/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: ; c-file-style: "linux" -*- */
package fabian1.keepalive;

public class KeepAlive
{
    private final int BIT_AND   = 0xff;
    private final int BIT_SHIFT = 8;
    
    private int   m_encoded_pulse;
    private int[] m_decoded_pulse;
    
    public KeepAlive( int i_robot_id, int other )
    {
	m_encoded_pulse =
	    ( i_robot_id & BIT_AND ) |
	    ( ( other & BIT_AND ) << BIT_SHIFT );
    }

    public KeepAlive( int i_encoded_pulse )
    {
	m_decoded_pulse = new int[ 2 ];
	m_decoded_pulse[ 0 ] = i_encoded_pulse & BIT_AND;
	m_decoded_pulse[ 1 ] = ( i_encoded_pulse >> BIT_SHIFT ) & BIT_AND;
    }
    
    public int encoded32()
    { return m_encoded_pulse; }
    
    public int[] decoded32()
    { return m_decoded_pulse; }
}
