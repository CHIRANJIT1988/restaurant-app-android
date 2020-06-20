package restaurent.menu.demo.helper;


import java.io.Serializable;


public class Employee implements Serializable
{

	public static String name, password, deviceId, mobileNo, gcmId;


	public Employee()
	{
		
	}


	public void setEmployeeName(String name)
	{
		this.name = name;
	}

	public String getEmployeeName()
	{
		return this.name;
	}


	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}
	
	public String getDeviceId()
	{
		return this.deviceId;
	}
	
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getPassword()
	{
		return this.password;
	}


	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getMobileNo()
	{
		return this.mobileNo;
	}
}