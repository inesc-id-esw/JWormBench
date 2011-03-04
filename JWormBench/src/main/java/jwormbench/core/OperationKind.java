package jwormbench.core;

public enum OperationKind {
  /* 0 */ Sum, 
  /* 1 */ Average, 
  /* 2 */ Median, 
  /* 3 */ Minimum, 
  /* 4 */ Maximum, 
  /* 5 */ ReplaceMaxWithAverage, 
  /* 6 */ ReplaceMinWithAverage, 
  /* 7 */ ReplaceMedianWithAverage, 
  /* 8 */ ReplaceMedianWithMax,
  /* 9 */ ReplaceMedianWithMin, 
  /* 10 */ ReplaceMaxAndMin, 
  /* 11 */ Sort, 
  /* 12 */ Transpose,
  /* 13 */ Checkpoint,  // not implemented
  /* 14 */ Undo,        // not implemented
  /* 15 */ LeaveMessage // not implemented
}
