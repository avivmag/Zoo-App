using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class WallFeed
    {
        public int id { get; set; }
        public DateTime created { get; set; }
        public string info { get; set; }
        public int language { get; set; }
    }
}
