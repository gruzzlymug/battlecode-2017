/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: ; c-file-style: "linux" -*- */
package fabian1.keepalive;

public class KeepAlive
{
    private int m_encoded_pulse;
    
    public KeepAlive( int i_robot_id )
    {
	String tmp = Integer.toHexString( i_robot_id );
	System.out.print( "ID = " );
	System.out.print( tmp );
	System.out.print( "\n" );

	m_encoded_pulse = 0;
    }
    
    public int Encode32()
    {
	return m_encoded_pulse;
    }

}
