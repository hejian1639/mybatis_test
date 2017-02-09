import org.apache.ibatis.session.SqlSession;

public class AppTest {
    private TransactionTokenMapper mapper = null;
    private SqlSession session = null;

    public static void main(String[] args) {
        App.init();

        SqlSession s = App.factory.openSession();

        TransactionTokenMapper mapper = s.getMapper(TransactionTokenMapper.class);
        mapper.schema();

        s.commit();
        s.close();

        AppTest test = new AppTest();

        test.setupSession();
        
        test.testInsert();
        test.testUpdate();
        test.testDeleteById();
        test.testRollback();
        
        test.closeSession();
    }

    private static TransactionToken tokenFactory(String tokenPrefix, String transactionPrefix) {
        TransactionToken t = new TransactionToken();
        t.setToken(tokenPrefix + System.currentTimeMillis());
        t.setTransaction(transactionPrefix + System.currentTimeMillis());
        return t;
    }

    public void testInsert() {
        TransactionToken t = tokenFactory("alpha", "beta");
        mapper.insert(t);

        TransactionToken t2 = tokenFactory("cappa", "delta");
        mapper.insert(t2);

        System.out.println(mapper.count());
    }

    public void setupSession() {
        session = App.factory.openSession(); // This obtains a database
                                             // connection!
        mapper = session.getMapper(TransactionTokenMapper.class);
    }

    public void closeSession() {
        session.commit(); // This commits the data to the database. Required
                          // even if auto-commit=true
        session.close(); // This releases the connection
    }

    public void testUpdate() {
        TransactionToken t = tokenFactory("faraday", "gamma");
        mapper.insert(t);

        TransactionToken t2 = mapper.getById(t.getId());

        t2.setToken("bingo" + System.currentTimeMillis());
        t2.setTransaction("funky" + System.currentTimeMillis());
        mapper.update(t2);

        TransactionToken t3 = mapper.getById(t.getId());
    }

    public void testDeleteById() {
        long count = mapper.count();

        TransactionToken t = tokenFactory("indigo", "jakarta");
        mapper.insert(t);

        mapper.deleteById(t);
    }

    public void testDeleteByTransaction() {
        long count = mapper.count();

        TransactionToken t2 = tokenFactory("kava", "lambda");
        mapper.insert(t2);

        mapper.deleteByTransaction(t2);
    }

    public void testFindByTransaction() {
        TransactionToken t = tokenFactory("manual", "nova");
        mapper.insert(t);

        TransactionToken t2 = mapper.selectByTransaction(t.getTransaction());
    }

    public void testRollback() {

        session.commit(); // This commits the data to the database. Required
        TransactionToken t = tokenFactory("omega", "passport");
        mapper.insert(t);
        System.out.println(mapper.count());

        session.rollback();
        System.out.println(mapper.count());

        TransactionToken t3 = tokenFactory("quark", "star");
        mapper.insert(t3);
        
        System.out.println(mapper.count());
        
    }

}
