using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend.Models;

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
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets animal by its Id, in the given language.
        /// </summary>
        /// <param name="id">The animal's Id.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The animal with the Id with that language.</returns>
        [HttpGet]
        [Route("animals/{language}/{id}")]
        public Animal GetAnimalById(int language, int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalById(language, id);
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets animal by its name, in the given language.
        /// </summary>
        /// <param name="name">The animal's name.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The animal with the Id with that language.</returns>
        [HttpGet]
        [Route("animals/{name}/{language}")]
        public Animal GetAnimalByName(string name, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalByName(name, language);
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }


        /// <summary>
        /// Gets all the animals that corresponds to the eclosure id and the give langauge.
        /// </summary>
        /// <param name="encId">The enclosure id.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The animals that are in the enclosure.</returns>
        [HttpGet]
        [Route("animals/enclosure/{encId}/{language}")]
        public IEnumerable<Animal> GetAnimalsByEnclosure(int encId, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalsByEnclosure(encId, language);
                }
            }
            catch (ArgumentException argExp)
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        #endregion

        #region Setters

        /// <summary>
        /// Adds or updates an animal.
        /// </summary>
        /// <param name="animals">The animal to update.</param>
        [HttpPost]
        [Route("animals/update")]
        public void UpdateAnimal(Animal animal)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateAnimal(animal);
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Removes an animal.
        /// </summary>
        /// <param name="id">The animals to delete.</param>
        [HttpPost]
        [Route("animals/{id}")]
        public void DeleteAnimal(int id)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteAnimal(id);
                }
            }
            //TODO: add catch Invalid operation exception
            catch 
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion
    }
}
