package jwormbench.core;

public interface IBenchWorld {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// -------------------  PROPERTIES   ----------------- 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * @return Number of rows.
   */
	int getRowsNum();
	/**
	 * @return Number of Columns.
	 */
	int getColumnsNum();
	/**
   * @return Node with coordinates 'x' and 'y'.
   */
	IBenchWorldNode getNode(int x, int y);
	/**
	 * @return Node on coordinate 'c'
	 */
	IBenchWorldNode getNode(ICoordinate c);
	/**
	 * The sum of all nodes values.
	 */
	int getSumOfAllNodes();
}