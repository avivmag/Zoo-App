using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using NegevZoo.Models;

namespace NegevZoo.Controllers
{
    public class AnimalController : ApiController
    {
        Animal[] animals = new[]
        {
            new Animal { Id = 5, name = "baboon", description = "I R BABOON", enclosure = "Monkeys" },
            new Animal { Id = 6, name = "weasel", description = "I AM WEASEL", enclosure = "maniacs" },
            new Animal { Id = 7, name = "donkey", description = "donkeykong", enclosure = "donkeys" },
            new Animal { Id = 8, name = "kingkong", description = "kingkong", enclosure = "Monkeys" }
        };

        [HttpGet]
        [Route("animals")]
        public IHttpActionResult GetAllAnimals()
        {
            return Ok(this.animals);
        }
    }
}
