package Utils;

public class test {
	public static void main(String[] args) {
	    try {
	        Class.forName("org.apache.http.client.methods.HttpRequestBase");
	        System.out.println("✅ Apache HttpClient is correctly loaded!");
	    } catch (ClassNotFoundException e) {
	        System.out.println("❌ Apache HttpClient is MISSING!");
	        e.printStackTrace();
	    }
	}

}
