import org.apache.ibatis.session.SqlSession;

import com.example.mybatis.TransactionToken;





public class AppTest {

    public static void main(String[] args) {
        App.init();


        SqlSession s = App.factory.openSession();

        TransactionTokenMapper mapper = s.getMapper(TransactionTokenMapper.class);
        mapper.schema();

        TransactionToken t = tokenFactory("alpha", "beta");
        mapper.insert(t);

        long count = mapper.count();

        TransactionToken t2 = tokenFactory("cappa", "delta");
        mapper.insert(t2);
 
        
        s.commit();
        s.close();
        
    }
    
    private static TransactionToken tokenFactory(String tokenPrefix, String transactionPrefix)
    {
        TransactionToken t = new TransactionToken();
        t.setToken(tokenPrefix + System.currentTimeMillis());
        t.setTransaction(transactionPrefix + System.currentTimeMillis());
        return t;
    }

}
