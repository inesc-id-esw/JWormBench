/*    
CommandLineArgumentParser Copyright (C) 2008 Ferad Zyulkyarov

This library is free software; you can redistribute it and/or modify it under 
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 2.1 of the License, or (at your option) any
later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along
with this library; if not, write to the Free Software Foundation, Inc., 
59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
*/

//+----------------------------------------------------------------------------
//
// File Name: CommandLineArgumentException.cs
// Description: An expection thrown when a command line argument is missing
//              or some parsing error occurs.
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 04.02.2008
//
//-----------------------------------------------------------------------------

using System;

namespace Bsc
{
    /// <summary>
    /// An expection thrown when a command line argument is missing or some 
    /// parsing error occurs.
    /// </summary>
    public class CommandLineArgumentException : Exception
    {
        public CommandLineArgumentException()
            : base("CommandLineArgumentException: Unknown")
        {
        }

        public CommandLineArgumentException(string errorMessage)
            : base(errorMessage)
        {
        }
    }
}
