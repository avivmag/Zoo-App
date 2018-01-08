using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace NegevZoo.Models
{
    public class Animal
    {
        public int Id { get; set; }
        public string name { get; set; }
        public string enclosure { get; set; }
        public string description { get; set; }
    }
}