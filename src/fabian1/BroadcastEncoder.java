/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: ; c-file-style: "linux" -*- */
package fabian1.broadcastencoder;

public class BroadcastEncoder
{
    private final int HEX_AND    = 0xf;
    private final int HEX_SHIFT  = 4;
    private final int BYTE_AND   = 0xff;
    private final int BYTE_SHIFT = 8;

    private int AND;
    private int SHIFT;
    private int PARTITIONS;
    
    private int   m_encoded_int;
    private int[] m_decoded_int;
    
    public BroadcastEncoder( int i_first_byte, int i_second_byte )
    {
	AND        = BYTE_AND;
	SHIFT      = BYTE_SHIFT;
	PARTITIONS = 2;
	
	m_encoded_int =
	    ( i_first_byte & AND ) |
	    ( ( i_second_byte & AND ) << SHIFT );
	
	m_decoded_int = new int[ PARTITIONS ];
    }

    public BroadcastEncoder( int i_first_hex, int i_second_hex,
			     int i_third_hex, int i_fourth_hex )
    {
	AND        = HEX_AND;
	SHIFT      = HEX_SHIFT;
	PARTITIONS = 4;
	
	m_encoded_int =
	    ( i_first_hex & AND ) |
	    ( ( i_second_hex & AND ) << SHIFT ) |
	    ( ( i_third_hex & AND ) << ( SHIFT * 2 ) ) |
	    ( ( i_fourth_hex & AND ) << ( SHIFT * 3 ) );
	
	m_decoded_int = new int[ PARTITIONS ];
    }

    public BroadcastEncoder( int i_int_partitions )
    {
	if( i_int_partitions == 2 )
	    {
		AND        = BYTE_AND;
		SHIFT      = BYTE_SHIFT;
		PARTITIONS = 2;
	    }
	else if( i_int_partitions == 4 )
	    {
		AND        = HEX_AND;
		SHIFT      = HEX_SHIFT;
		PARTITIONS = 4;
	    }
	else
	    {
		PARTITIONS = 0;
		return;
	    }

	m_decoded_int = new int[ PARTITIONS ];
    }

    public int[] decode( int i_encoded32 )
    {
	m_encoded_int = i_encoded32;

	for( int part_i = 0; part_i < PARTITIONS; ++part_i )
	    {
		m_decoded_int[ part_i ] = i_encoded32 & AND;
		i_encoded32             = i_encoded32 >> SHIFT;
	    }

	return m_decoded_int;
    }
    
    public int encoded32()
    { return m_encoded_int; }
    
    public int[] decoded32()
    { return m_decoded_int; }
}
