using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class WallFeed
    {
        public int Id { get; set; }
        public DateTime Created { get; set; }
        public string Info { get; set; }
        public int Language { get; set; }
    }
}
