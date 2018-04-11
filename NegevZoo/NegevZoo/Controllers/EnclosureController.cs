using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using DAL;
using DAL.Models;
using BL;
using System.Web;
using System.IO;
using Newtonsoft.Json.Linq;

namespace NegevZoo.Controllers
{
    /// <summary>
    /// Controller that holds all enclosure functionality.
    /// </summary>
    public class EnclosureController : ControllerBase
    {

        #region Getters

        #region visitor app

        /// <summary>
        /// Gets all the enclosures resultes with data in that language.
        /// This method use for the visitore app to get all the enclosures with the wanted language.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <returns>All enclosures with that language.</returns>
        [HttpGet]
        [Route("enclosures/all/{language}")]
        public IEnumerable<EnclosureResult>  GetAllEnclosureResults(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllEnclosureResults(language);
                }
            }
            catch
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets enclosure by it's encId.
        /// This function uses the vistore app to get the enclosure data by it's id
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="id">The eclosure's encId</param>
        /// <returns>The enclosures with this encId and language.</returns>
        [HttpGet]
        [Route("enclosures/id/{encId}/{language}")]
        public EnclosureResult GetEnclosureById(int encId, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureById(encId, language);
                }

            }
            catch (Exception Exp)
            {
                //TODO: add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets enclosure by it's name.
        /// This funcion is used by the visitor application.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="name">The eclosure's name</param>
        /// <returns>The enclosures with this name and language.</returns>
        [HttpGet]
        [Route("enclosures/name/{name}/{language}")]
        public IEnumerable<EnclosureResult> GetEnclosureByName(string name, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureByName(name, language);
                }
            }
            catch (Exception Exp)
            {
                //TODO: add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets enclosure by it's position.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="langtitude">The eclosure's langtitude</param>
        /// <param name="latitude">The eclosure's latitude</param>
        /// <returns>The enclosures with this aproximate position.</returns>
        [HttpGet]
        [Route("enclosures/position/{longtitude}/{latitude}/{language}")]
        public Enclosure GetEnclosureByPosition(int longtitude, int latitude, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureByPosition(longtitude, latitude, language);
                }

            }
            catch (Exception Exp)
            {
                //TODO: add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        /// <summary>
        /// Gets all the enclosures types.
        /// </summary>
        /// <returns>All enclosures types.</returns>
        [HttpGet]
        [Route("enclosures/types/all")]
        public IEnumerable<Enclosure> GetAllEnclosures()
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetAllEnclosures();
                }

            }
            catch (Exception Exp)
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets all the enclosures types with data in that language.
        /// </summary>
        /// <param name="encId">The enclosure id</param>
        /// <returns>All the details to this enclosure id.</returns>
        [HttpGet]
        [Route("enclosures/details/all/{encId}")]
        public IEnumerable<EnclosureDetail>  GetEnclosureDetailsById(int encId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureDetailsById(encId);
                }

            }
            catch (Exception Exp)
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets the enclosure's recurring events by it's encId.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="encId">The eclosure's encId</param>
        /// <returns>The recurring events according to the enclosure encId.</returns>
        [HttpGet]
        [Route("enclosures/recurring/{encId}/{language}")]
        public IEnumerable<RecurringEvent> GetRecurringEvents(int encId, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetRecurringEvents(encId, language);
                }
            }
            catch (Exception Exp)
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets the enclosure's pictures by it's id.
        /// </summary>
        /// <param name="encId">The eclosure's encId</param>
        /// <returns>The enclosure pictures according to its id.</returns>
        [HttpGet]
        [Route("enclosures/pictures/{encId}")]
        public IEnumerable<EnclosurePicture> GetEnclosurePicturesById(int encId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosurePicturesById(encId);
                }
            }
            catch (Exception Exp)
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets the enclosure's videos urls by it's encId.
        /// </summary>
        /// <param name="encId">The eclosure's encId</param>
        /// <returns>The enclosures video urls according to it's id.</returns>
        [HttpGet]
        [Route("enclosures/videos/{encId}")]
        public IEnumerable<YoutubeVideoUrl> GetEnclosureVideosById(int encId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureVideosById(encId);
                }
            }
            catch (Exception Exp)
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #region Setters

        /// <summary>
        /// Adds or updates an enclosure.
        /// </summary>
        /// <param name="enclosures">The enclosures to update.</param>
        [HttpPost]
        [Route("enclosures/update")]
        public void UpdateEnclosure(Enclosure enclosure)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateEnclosure(enclosure);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Adds or updates an enclosure details.
        /// </summary>
        /// <param name="enclosureDetail">The enclosures to update.</param>
        [HttpPost]
        [Route("enclosures/detail/update")]
        public void UpdateEnclosureDetail(EnclosureDetail enclosureDetail)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateEnclosureDetails(enclosureDetail);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Adds or updates an enclosure picture.
        /// </summary>
        /// <param name="enclosurePicture">The enclosures to update.</param>
        [HttpPost]
        [Route("enclosures/picture/update")]
        public void UpdateEnclosurePicture(EnclosurePicture enclosurePicture)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateEnclosurePicture(enclosurePicture);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }
        
        /// <summary>
        /// Adds or updates an enclosure video.
        /// </summary>
        /// <param name="enclosureVideo">The enclosures to update.</param>
        [HttpPost]
        [Route("enclosures/video/update")]
        public void UpdateEnclosureVideo(YoutubeVideoUrl enclosureVideo)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateEnclosureVideo(enclosureVideo);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Adds or updates a recurring event element.
        /// </summary>
        /// <param name="recEvent">The RecurringEvent element to update or add.</param>
        [HttpPost]
        [Route("enclosures/recurring/update")]
        public void UpdateRecurringEvent(RecurringEvent recEvent)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateRecurringEvent(recEvent);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Deletes an enclosure.
        /// </summary>
        /// <param name="id">The enclosure's encId to delete.</param>
        [HttpDelete]
        [Route("enclosures/delete/{encId}")]
        public void DeleteEnclosure(int encId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteEnclosure(encId);
                }

            }
            catch (Exception Exp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// delete an enclosure picture.
        /// </summary>
        /// <param name="enclosurePictureId">The EnclosurePicture's id to delete.</param>
        [HttpDelete]
        [Route("enclosures/picture/delete")]
        public void DeleteEnclosurePicture(int enclosurePictureId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteEnclosurePicture(enclosurePictureId);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        /// <summary>
        /// delete an enclosure video.
        /// </summary>
        /// <param name="enclosureVideoId">The EnclosureVideo's id to delete.</param>
        [HttpDelete]
        [Route("enclosures/video/delete")]
        public void DeleteEnclosureVideo(int enclosureVideoId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteEnclosureVideo(enclosureVideoId);
                }

            }
            catch (Exception Exp)
            {
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }
        
        [HttpDelete]
        [Route("enclosures/recurring/delete/{eventId}")]
        public void DeleteRecurringEvent(int eventId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteRecurringEvent(eventId);
                }

            }
            catch (Exception Exp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

       
        #endregion

        [HttpPost]
        [Route("enclosures/upload/{path}")]
        public IHttpActionResult EnclosureImagesUpload(String path)
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
                    db.ImagesUpload(httpRequest, @"~/assets/enclosures/" + path);
                    return Ok();
                }
            }
            catch (Exception exp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }

            //var fileNames = new List<String>();
            
            //foreach (string file in httpRequest.Files)
            //{
            //    var postedFile      = httpRequest.Files[file];

            //    var fileExtension   = postedFile.FileName.Split('.').Last();
            //    var fileName        = Guid.NewGuid() + "." + fileExtension;

            //    var filePath = HttpContext.Current.Server.MapPath(@"~/assets/" + fileName);

            //    postedFile.SaveAs(filePath);

            //    fileNames.Add(fileName);
            //}

            //var responseObject = new JArray();

            //foreach (var fn in fileNames)
            //{
            //    responseObject.Add(new JValue("assets/" + fn));
            //}

            //return Ok(responseObject);
        }
    }
}
