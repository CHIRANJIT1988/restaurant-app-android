package restaurent.menu.demo.configuration;

public class ConnectionString 
{

	private static String SERVER_URL = "";
	public static final String IMAGE_URL = "http://wildorchidsadventures.com/restaurent_api/item_pic/";

	public static final String API_URL = "http://www.wildorchidsadventures.com/restaurent_api/";

	public ConnectionString()
	{
		
		// SERVER_URL = "http://192.168.43.53:81/gmc_manasa_api/";

		// SERVER_URL = "http://172.16.80.12:81/gmc_manasa_api/";

		SERVER_URL = "http://www.wildorchidsadventures.com/restaurent_api/";
	}
	
	
	public String getURL()
	{
		return SERVER_URL;
	}
}