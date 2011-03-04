package jwormbench.core;

public interface IWorld {

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
	INode getNode(int x, int y);
	/**
	 * @return Node on coordinate 'c'
	 */
	INode getNode(ICoordinate c);
	/**
	 * The sum of all nodes values.
	 */
	int getSumOfAllNodes();
}