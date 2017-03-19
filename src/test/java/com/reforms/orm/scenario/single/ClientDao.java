package com.reforms.orm.scenario.single;

import com.reforms.orm.H2DataSource;
import com.reforms.orm.OrmDao;

import java.util.List;

/**
 * Слой доступа к данным по клиенту
 * @author evgenie
 */
public class ClientDao {

    private OrmDao ormDao;

    public ClientDao(H2DataSource h2ds) {
        ormDao = new OrmDao(h2ds);
    }

    private static final String LOAD_CLIENT_IDS_QUERY =
            "SELECT cl.id FROM client AS cl ORDER BY 1 ASC";

    /**
     * Загрузить информацию о клиенте
     * @param clientId
     * @return
     */
    public List<Long> loadClientIds() throws Exception {
        return ormDao.loadSimpleOrms(Long.class, LOAD_CLIENT_IDS_QUERY);
    }

    private static final String LOAD_CLIENT_NAMES_QUERY =
            "SELECT cl.name AS clientName FROM client AS cl ORDER BY 1 ASC";

    /**
     * Загрузить информацию о клиенте
     * @param clientId
     * @return
     */
    public List<String> loadClientNames() throws Exception {
        return ormDao.loadOrms(String.class, LOAD_CLIENT_NAMES_QUERY);
    }

    private static final String LOAD_CLIENT_CLIENT_ORDER_QUERY =
            "SELECT cl.id FROM client AS cl ORDER BY 1 ASC";

    /**
     * Загрузить информацию о клиенте
     * @param clientId
     * @return
     */
    public List<ClientOrder> loadClientOrders() throws Exception {
        return ormDao.loadOrms(ClientOrder.class, LOAD_CLIENT_CLIENT_ORDER_QUERY);
    }
}