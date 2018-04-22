using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using DAL;
using DAL.Models;
using Newtonsoft.Json.Linq;

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
        /// <returns> All the AnimalsResult with that langauge.</returns>
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
        /// <returns>All the AnimalsResults that have a spiceial story in the wanted langauge.</returns>
        [HttpGet]
        [Route("animals/story/{language}")]
        public IEnumerable<AnimalResult> GetAnimalResultsWithStory(int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAnimalResultsWithStory(language);
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
        /// Gets all the animals that corresponds to the eclosure animalId and the give langauge.
        /// </summary>
        /// <param name="encId">The enclosure animalId.</param>
        /// <param name="language">The data language.</param>
        /// <returns>AnimalResults of animals that are in the enclosure.</returns>
        [HttpGet]
        [Route("animals/enclosure/{encId}")]
        public IEnumerable<Animal> GetAnimalsByEnclosure(int encId)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalsByEnclosure(encId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets AnimalResult of the animals that conatin the given name, in the given language.
        /// </summary>
        /// <param name="name">The animal's name.</param>
        /// <param name="language">The data language.</param>
        /// <returns>The AnimalReuslts of the animals that contain the given name with the Id with that language.</returns>
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

        #endregion

        /// <summary>
        /// This method is for the worker site.
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
        /// Gets all the existing AnimalDetails of the animal with the given id.
        /// </summary>
        /// <returns>Animaldetails in all the langauges exists.</returns>
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
        
        #endregion
        
        #region Setters

        /// <summary>
        /// This method is for the Worker site.
        /// Adds or updates an animal.
        /// </summary>
        /// <param name="animal">The animal to add or update.</param>
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
        /// This method is for the Worker site.
        /// Adds or updates an animal details.
        /// </summary>
        /// <param name="animalsDetails">The AnimalDetail to update.</param>
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

        /// <summary>
        /// Save a file to the data base.
        /// </summary>
        /// <param name="path"> the path to save the file</param>
        /// <returns>action result of the operation. </returns>
        [HttpPost]
        [Route("animals/upload/{path}")]
        public JArray AnimalsFileUpload(String path)
        {
            if (String.IsNullOrWhiteSpace(path))
            {
                throw new Exception("Wrong input. Path is empty");
            }

            var httpRequest = HttpContext.Current.Request;

            if (httpRequest.Files.Count < 1)
            {
                throw new ArgumentNullException("No files were selected to upload");
            }

            try
            {
                using (var db = GetContext())
                {
                    return db.FileUpload(httpRequest, @"~/assets/animals/" + path + '/');
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion
    }
}
