using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend;
using Backend.Models;

namespace NegevZoo.Controllers
{
    public class AnimalController : ApiController
    {
        private DummyDB db = new DummyDB();

        [HttpGet]
        [Route("animals/{language}")]
        public IEnumerable<Animal> GetAllAnimals(int language = 1)
        {
            try
            {
                return db.GetAnimals(language);

            }
            catch
            {
                return null;
            }
        }

        [HttpGet]
        [Route("enclosures/{language}")]
        public IEnumerable<Enclosure> GetAllEnclosures(int language = 1)
        {
            try
            {
                return db.GetEnclosures(language);

            }
            catch
            {
                return null;
            }
        }

        [HttpPost]
        [Route("animals")]
        public void UpdateAnimals(IEnumerable<Animal> animals)
        {
            try
            {
                db.SetAnimals(animals);
            }
            catch
            {
            }
        }

        [HttpPost]
        [Route("enclosures")]
        public void UpdateEnclosures(IEnumerable<Enclosure> enclosures)
        {
            try
            {
                db.SetEnclosures(enclosures);

            }
            catch
            {
            }
        }
    }
}
