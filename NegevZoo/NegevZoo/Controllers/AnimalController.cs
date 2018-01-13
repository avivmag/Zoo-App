using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend;
using Backend.Models;
using BL;

namespace NegevZoo.Controllers
{
    /// <summary>
    /// Controller that holds all animal functionality.
    /// </summary>
    public class AnimalController : ControllerBase
    {
        #region Getters

        /// <summary>
        /// Gets all animals data in the given langauge.
        /// </summary>
        /// <param name="language">The data language.</param>
        /// <returns>Animals with that langauge.</returns>
        [HttpGet]
        [Route("animals/{language}")]
        public IEnumerable<Animal> GetAllAnimals(int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAnimals(language);
                }
            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets the animal by its Id, in the given language.
        /// </summary>
        /// <param name="id">The animal's Id.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The animal with the Id with that language.</returns>
        [HttpGet]
        [Route("animals/{id}/{language}")]
        public Animal GetAnimalById(int id, int language)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAnimalById(language, id);
                }
            }
            catch
            {
                return null;
            }
        }

        #endregion

        #region Setters

        /// <summary>
        /// Updates the animals list.
        /// </summary>
        /// <param name="animals">The animals to update to.</param>
        [HttpPut]
        [Route("animals")]
        public void UpdateAnimals(IEnumerable<Animal> animals)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.AddAnimals(animals);
                }
            }
            catch
            {
            }
        }

        #endregion
    }
}
