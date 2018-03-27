package im.icbc.cn.keyboard.utils;

import java.util.Random;
import java.util.Vector;

public class RandomValue 
{
	public static Vector<String> m_vsValues = null;
	
	public static boolean initValues( String[] values )
	{
		if( values == null || values.length <= 0 )
			return false;
		
		if( m_vsValues == null )
			m_vsValues = new Vector<String>();
		else
			m_vsValues.clear();
		
		int len = values.length;
		for( int i = 0; i < len; i++ )
		{
			if( values[i] != null )
				m_vsValues.add(values[i]);
				
		}
		
		return true; 
	}
	
	public static String getRandomValue()
	{
		if( m_vsValues == null || m_vsValues.size() <= 0 )
			return null;
		
		Random randGen = new Random();
		
		int r = randGen.nextInt(m_vsValues.size());
		
		String ret = m_vsValues.get(r);
		
		m_vsValues.remove(r);
		
		return ret;
	}
	
	public static void cleanMemory()
	{
		if( m_vsValues != null )
			m_vsValues.clear();
	}

}
