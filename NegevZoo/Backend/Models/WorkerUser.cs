using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class WorkerUser
    {
        public int id { get; set; }
        public string name { get; set; }
        public string password { get; set; }
        public bool isAdmin { get; set; }
    }
}
