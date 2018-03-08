package com.sstjerne.geolocalization.message.persistence.client;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sstjerne.geolocalization.message.model.MessageModel;

@PropertySources(value = @PropertySource({ "classpath:application.properties" }) )
@Component
public class ESWrapperClient implements ESWrapperClientI<Client> {

	private static final String MESSAGE = "message";
	private static final String TIMESTAMP = "timestamp";

	private static Logger LOGGER = LoggerFactory.getLogger(ESWrapperClient.class.getName());

	@Autowired
	private Environment env;

	private Client client;
	
	private Integer maxItems;

	@PostConstruct
	public void initialize() throws Exception {

		boolean localMode = Boolean.parseBoolean(env.getProperty("es.local.mode"));
		this.maxItems = Integer.valueOf(env.getProperty("max.response.items"));
		String elasticSearchCluster = env.getProperty("es.cluster.name");
		if (localMode) {
			this.client = nodeBuilder().local(true).clusterName(elasticSearchCluster).node().client();
		} else {
			String host = (String) env.getProperty("es.host");
			int port = Integer.parseInt((String) env.getProperty("es.port"));

			Settings settings = ImmutableSettings.settingsBuilder().put("es.cluster.name", elasticSearchCluster)
					.build();
			LOGGER.info("Initializing transport client with following properties: {}", settings);
			TransportClient transportClient = new TransportClient(settings);
			this.client = transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
		}

	}

	@Override
	public void restart() throws Exception {
		this.client.close();
	}

	@Override
	public void finalize() throws Throwable {
		this.client.close();
	}

	@Override
	public Client getClient() {
		return client;
	}

	@Override
	public void verifyConnection() throws Exception {
		ImmutableList<DiscoveryNode> nodes = ((TransportClient) this.client).connectedNodes();
		if (nodes.isEmpty()) {
			throw new Exception("No nodes available. Verify ES is running!");
		} else {
			LOGGER.info("connected to nodes: " + nodes.toString());
		}
	}

	@Override
	public boolean save(String country, String message, Long timestamp) {
		LOGGER.debug("Entering save method country={}, message={}", country, message);

		if (!isIndexExists(country)) {
			CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(country);
			CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
		}
		try {
			XContentBuilder xContentBuilder = JsonXContent.contentBuilder().startObject().field(MESSAGE, message)
					.field(TIMESTAMP, timestamp).endObject();
			IndexResponse response = client.prepareIndex(country, MESSAGE, UUID.randomUUID().toString())
					.setSource(xContentBuilder).execute().actionGet();

			LOGGER.debug("Entering save method country={}, message={}, isCreated={}", country, message,
					response.isCreated());

		} catch (IOException e) {
			LOGGER.error("put->" + e.getMessage());
			return false;

		}
		return true;

	}

	/**
	 * @param indexName
	 * @return
	 */
	public boolean isIndexExists(String indexName) {

		ActionFuture<IndicesExistsResponse> exists = client.admin().indices()
				.exists(new IndicesExistsRequest(indexName));

		IndicesExistsResponse actionGet = exists.actionGet();

		return actionGet.isExists();
	}

	@Override
	public List<MessageModel> get(int numOf) {
		LOGGER.debug("Entering get method numOf={}, lang=all", numOf);
		List<MessageModel> result = new ArrayList<MessageModel>();
		try {
			SearchResponse response = client.prepareSearch()
					.addSort(SortBuilders.fieldSort(TIMESTAMP).order(SortOrder.DESC))
					.setSize(numOf < maxItems ?  numOf : maxItems )
					.execute().actionGet();
			SearchHit[] results = response.getHits().getHits();
			for (SearchHit hit : results) {
				Map<String, Object> map = hit.getSource();
				result.add(new MessageModel((String) map.get(MESSAGE), (Long) map.get(TIMESTAMP)));

			}
		} catch (Exception e) {
			LOGGER.error("put->" + e.getMessage());
		}
		return result;
	}

	@Override
	public List<MessageModel> get(int numOf, String lang) {
		LOGGER.debug("Entering get method numOf={}, lang={}", numOf, lang);
		List<MessageModel> result = new ArrayList<MessageModel>();
		
		try {
			SearchResponse response = client.prepareSearch(lang)
					.addSort(SortBuilders.fieldSort(TIMESTAMP).order(SortOrder.DESC))
					.setFrom(0)
					.setSize(numOf < maxItems ?  numOf : maxItems )
					.execute().actionGet();

			SearchHit[] results = response.getHits().getHits();
			for (SearchHit hit : results) {
				Map<String, Object> map = hit.getSource();
				result.add(new MessageModel((String) map.get(MESSAGE), (Long) map.get(TIMESTAMP)));

			}
		} catch (Exception e) {
			LOGGER.error("put->" + e.getMessage());
		}

		return result;
	}

}
