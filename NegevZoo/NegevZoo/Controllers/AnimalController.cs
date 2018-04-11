using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using DAL;
using DAL.Models;

namespace NegevZoo.Controllers
{
    /// <summary>
    /// Controller that holds all animal functionality.
    /// </summary>
    public class AnimalController : ControllerBase
    {
        #region Getters

        #region Visitors App
        /// <summary>
        /// Gets all animals data in the given langauge.
        /// </summary>
        /// <param name="language">The data language.</param>
        /// <returns>Animals with that langauge.</returns>
        [HttpGet]
        [Route("animals/all/{language}")]
        public IEnumerable<AnimalResult> GetAllAnimalsResults(int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAnimalsResults(language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets all animals that have special story.
        /// </summary>
        /// <param name="language">The data language.</param>
        /// <returns>Animals with spiceial story in the wanted langauge.</returns>
        [HttpGet]
        [Route("animals/story/{language}")]
        public IEnumerable<AnimalResult> GetAnimalsWithStoryResults(int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAnimalsWithStoryResults(language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        /// <summary>
        /// Gets all animals types.
        /// </summary>
        /// <returns>Animals types.</returns>
        [HttpGet]
        [Route("animals/types/all")]
        public IEnumerable<Animal> GetAllAnimals()
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAllAnimals();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets all animals details by the given id.
        /// </summary>
        /// <returns>Animals detail in all the langauges exists.</returns>
        [HttpGet]
        [Route("animals/details/all/{animalId}")]
        public IEnumerable<AnimalDetail> GetAllAnimalsDetailsById(int animalId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAllAnimalsDetailById(animalId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
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
        [Route("animals/animalId/{animalId}/{language}")]
        public AnimalResult GetAnimalById(int animalId, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalById(animalId, language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
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
        [Route("animals/name/{name}/{language}")]
        public IEnumerable<AnimalResult> GetAnimalByName(string name, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalByName(name, language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets all the animals that corresponds to the eclosure animalId and the give langauge.
        /// </summary>
        /// <param name="encId">The enclosure animalId.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The animals that are in the enclosure.</returns>
        [HttpGet]
        [Route("animals/enclosure/{encId}/{language}")]
        public IEnumerable<AnimalResult> GetAnimalsByEnclosure(int encId, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalsByEnclosure(encId, language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
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
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Adds or updates an animal details.
        /// </summary>
        /// <param name="animalsDetails">The animal to update.</param>
        [HttpPost]
        [Route("animals/detail/update")]
        public void UpdateAnimalDetails(AnimalDetail animalsDetails)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateAnimalDetails(animalsDetails);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Removes an animal.
        /// </summary>
        /// <param name="id">The animals to delete.</param>
        [HttpDelete]
        [Route("animals/delete/{animalId}")]
        public void DeleteAnimal(int animalId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteAnimal(animalId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        [HttpPost]
        [Route("animals/upload/{path}")]
        public IHttpActionResult AnimalsFileUpload(String path)
        {
            if (String.IsNullOrWhiteSpace(path))
            {
                throw new Exception("Wrong input. Path is empty");
            }

            var httpRequest = HttpContext.Current.Request;

            if (httpRequest.Files.Count < 1)
            {
                return BadRequest();
            }

            try
            {
                using (var db = GetContext())
                {
                    db.FileUpload(httpRequest, @"~/assets/animals/" + path);
                    return Ok();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

    }
}
