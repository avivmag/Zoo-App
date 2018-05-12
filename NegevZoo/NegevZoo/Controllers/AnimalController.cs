using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Security.Authentication;
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Language:" + language);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Language:" + language);
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
        [Route("animals/enclosure/{encId}/{language}")]
        public IEnumerable<AnimalResult> GetAnimalResultByEnclosure(int encId, int language)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalResultByEnclosure(encId, language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id:" + encId + ", language" + language);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets all the animals stroies data in the given langauge.
        /// </summary>
        /// <param name="language">The data language.</param>
        /// <returns> All the AnimalStoryResults with that language.</returns>
        [HttpGet]
        [Route("animals/story/all/{language}")]
        public IEnumerable<AnimalStoryResult> GetAllAnimalStoriesResults(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllAnimalStoriesResults(language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Language: " + language);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets a single AnimalStory object in the given langauge.
        /// </summary>
        /// <param name="animalStoryId">The wanted AnimalStory id</param>
        /// <param name="language">The data language.</param>
        /// <returns> The AnimalStoryResults object that corresponds to the given Id with that language.</returns>
        [HttpGet]
        [Route("animals/story/{animalStoryId}/{language}")]
        public AnimalStoryResult GetAnimalStoryResultById(int animalStoryId, int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAnimalStoryResultById(animalStoryId, language);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, 
                    "Animal story Id: " + animalStoryId + ", language: " + language);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method is for the worker site.
        /// Gets all the existing AnimalDetails of the animal with the given id.
        /// </summary>
        /// <param name="animalId">The id of the wanted AnimalDetails</param>
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Animal Id:" + animalId);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id:" + encId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method is for the worker site.
        /// Gets all AnimalStory types exists.
        /// </summary>
        /// <returns>All AnimalStory types.</returns>
        [HttpGet]
        [Route("animals/story/all")]
        public IEnumerable<AnimalStory> GetAllAnimalStories()
        {
            try
            {
                using(var db = GetContext())
                {
                    return db.GetAllAnimalStories();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method is for the worker site.
        /// Gets all the existing AnimalStoryDetails of the AnimalStory with the given id.
        /// </summary>
        /// <param name="animalStoryId">The id of the wanted AnimalStory</param>
        /// <returns>Animaldetails in all the langauges exists.</returns>
        [HttpGet]
        [Route("animals/story/details/all/{animalStoryId}")]
        public IEnumerable<AnimalStoryDetail> GetAllAnimalStoryDetailsById(int animalStoryId)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllAnimalStoryDetailsById(animalStoryId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "animalStoryId: " + animalStoryId);
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
                    if (ValidateSessionId(db))
                    {
                        db.UpdateAnimal(animal);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                string animalInput = "Id: " + animal.id + ", name: " + animal.name + ", encId: " + animal.enclosureId + ", preservation: " + animal.preservation + ", picutre URL: " + animal.pictureUrl;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Animal: " + animalInput);
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
                    if (ValidateSessionId(db))
                    {
                        db.UpdateAnimalDetails(animalsDetails);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                string AnimalDetailInput = "id: " + animalsDetails.animalId + ", name: " + animalsDetails.name + ", category: " + animalsDetails.category + ", distribution: " + animalsDetails.distribution + ", family: " + animalsDetails.family + ", food: " + animalsDetails.food + animalsDetails.reproduction + ", series: " + animalsDetails.series + ", language: " + animalsDetails.language; 

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "AnimalDetails: " + AnimalDetailInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method is for the Worker site.
        /// Adds or updates an AnimalStory object.
        /// </summary>
        /// <param name="animalStory">The AnimalStory to add or update.</param>
        [HttpPost]
        [Route("animals/story/update")]
        public void UpdateAnimalStory(AnimalStory animalStory)
        {
            try
            {
                using (var db = GetContext())
                {
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }

                    db.UpdateAnimalStory(animalStory);
                }
            }
            catch (Exception Exp)
            {
                var animalStoryInput = "Id: " + animalStory.id + ", enclosure Id: " + animalStory.enclosureId + 
                    ", picture Url: " + animalStory.pictureUrl;
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, animalStoryInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method is for the Worker site.
        /// Adds or updates a single AnimalStoryDetail object.
        /// </summary>
        /// <param name="animalsDetails">The AnimalDetail to update.</param>
        [HttpPost]
        [Route("animals/story/detail/update")]
        public void UpdateAnimalStoryDetail(AnimalStoryDetail animalStoryDetail)
        {
            try
            {
                using (var db = GetContext())
                {
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }

                    db.UpdateAnimalStoryDetail(animalStoryDetail);
                }
            }
            catch (Exception Exp)
            {
                var animalStoryDetailInput = "Animal Stroy Id: " + animalStoryDetail.animalStoryId + ", name: " + animalStoryDetail.name +
                    ", story: " + animalStoryDetail.story + ", language: " + animalStoryDetail.language;
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, animalStoryDetailInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Removes an Animal object and all of it's corresponding AnimalDetails.
        /// </summary>
        /// <param name="animalId">The animals to delete.</param>
        [HttpDelete]
        [Route("animals/delete/{animalId}")]
        public void DeleteAnimal(int animalId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        db.DeleteAnimal(animalId);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Animal Id: " + animalId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Removes an AnimalStory objext and all it's corresponding AnimalStoryDetails.
        /// </summary>
        /// <param name="animalStoryId">The AnimalStory id to delete.</param>
        [HttpDelete]
        [Route("animals/story/delete/{animalStoryId}")]
        public void DeleteAnimalStory(int animalStoryId)
        {
            try
            {
                using (var db = GetContext())
                {
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }

                    db.DeleteAnimalStory(animalStoryId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Animal story Id: " + animalStoryId);
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
                    if (ValidateSessionId(db))
                    {
                        return db.FileUpload(httpRequest, @"~/assets/animals/" + path + '/');
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Path: " + path);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion
    }
}