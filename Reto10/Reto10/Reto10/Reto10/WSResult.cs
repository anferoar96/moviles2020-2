using System;
using System.Collections.Generic;
using System.Text;

namespace Reto10
{
    
    public class WSResult
    {
        public string ano { get; set; }
        public string periodo { get; set; }
        public string estrato_01 { get; set; }
        public string estrato_02 { get; set; }
        public string estrato_03 { get; set; }
        public string estrato_04 { get; set; }
        public string estrato_05 { get; set; }
        public string estrato_06 { get; set; }

    }

    public class Root
    {
        public List<WSResult> Test { get; set; }
    }

}
