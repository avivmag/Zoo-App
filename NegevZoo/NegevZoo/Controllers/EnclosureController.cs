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
using System.Net.Http.Headers;
using System.Security.Authentication;

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
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Language: " + language);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + encId);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + encId + ", langauge" + language);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + encId);
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + encId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #region Setters

        #region Update

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
                    if (ValidateSessionId(db))
                    {
                        db.UpdateEnclosure(enclosure);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }                    

                }

            }
            catch (Exception Exp)
            {
                string EnclosureInput = "Id: " + enclosure.id + "name: " + enclosure.name + 
                    "Icon Url: " + enclosure.markerIconUrl + "Marker Longitude: " + enclosure.markerLongitude +
                    "Marker Latitude: " + enclosure.markerLatitude + "Picture Url: " + enclosure.pictureUrl;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure: " + EnclosureInput);
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
                    if (ValidateSessionId(db))
                    {
                        db.UpdateEnclosureDetails(enclosureDetail);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                string enclosureDetailInput = "Enclosure Id: " + enclosureDetail.encId + ", name: " + enclosureDetail.name + ", story: " + enclosureDetail.story + ", language: " + enclosureDetail.language;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Details: " + enclosureDetailInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Adds or updates an enclosure video.
        /// </summary>
        /// <param name="enclosureVideo">The enclosures to update.</param>
        [HttpPost]
        [Route("enclosures/video/update")]
        public YoutubeVideoUrl UpdateEnclosureVideo(YoutubeVideoUrl enclosureVideo)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        return db.UpdateEnclosureVideo(enclosureVideo);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                string videoInput = "Id: " + enclosureVideo.id + ", enclosure Id: " + enclosureVideo.enclosureId + ", video Url: " + enclosureVideo.videoUrl;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure video: " + videoInput);
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
                    if (ValidateSessionId(db))
                    {
                        db.UpdateRecurringEvent(recEvent);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                string recEventInput = "Id: " + recEvent.id + ", title: " + recEvent.title + ", description: " + recEvent.description + ", enclosure Id: " + recEvent.enclosureId + ", day: " + recEvent.day + ", start time: " + recEvent.startTime + ", end time: " + recEvent.endTime + ", language: " + recEvent.language;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Recurring Event: " + recEventInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #region Deletion

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
                    if (ValidateSessionId(db))
                    {
                        db.DeleteEnclosure(encId);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + encId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// delete an enclosure picture.
        /// </summary>
        /// <param name="enclosurePictureId">The EnclosurePicture's id to delete.</param>
        [HttpDelete]
        [Route("enclosures/{enclosureId}/picture/{pictureId}/delete")]
        public void DeleteEnclosurePicture(int enclosureId, int pictureId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        db.DeleteEnclosurePicture(enclosureId, pictureId);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + enclosureId + ", picture Id: " + pictureId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        /// <summary>
        /// delete an enclosure video.
        /// </summary>
        /// <param name="enclosureVideoId">The EnclosureVideo's id to delete.</param>
        [HttpDelete]
        [Route("enclosures/{enclosureId}/video/{enclosureVideoId}/delete")]
        public void DeleteEnclosureVideo(int enclosureId, int enclosureVideoId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        db.DeleteEnclosureVideo(enclosureId, enclosureVideoId);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + enclosureId + ", video Id: " + enclosureVideoId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        [HttpDelete]
        [Route("enclosures/{enclosureId}/recurring/delete/{eventId}")]
        public void DeleteRecurringEvent(int enclosureId, int eventId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        db.DeleteRecurringEvent(enclosureId, eventId);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }

            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + enclosureId + ", event Id: " + eventId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #endregion

        #region File Upload

        [HttpPost]
        [Route("enclosures/{enclosureId}/upload/bulk")]
        public IEnumerable<EnclosurePicture> EnclosureBulkPicturesUpload(int enclosureId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    // check that the session is leagal
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                    
                    // Get the enclosure.
                    var enclosure = db.GetAllEnclosures().SingleOrDefault(e => e.id == enclosureId);
                    
                    // If no such enclosure exists, throw error.
                    if (enclosure == default(Enclosure))
                    {
                        throw new ArgumentException("No enclosure with such enclosure Id exists.");
                    }

                    // Complete the upload procedure.
                    var uploadedPictures    = EnclosureImagesUpload("pictures");

                    return db.AddEnclosurePictures(enclosureId, uploadedPictures);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Enclosure Id: " + enclosureId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        [HttpPost]
        [Route("enclosures/upload/{path}")]
        public JArray EnclosureImagesUpload(String path)
        {
            if (String.IsNullOrWhiteSpace(path))
            {
                throw new Exception("Wrong input. Path is empty");
            }

            var httpRequest = HttpContext.Current.Request;

            if (httpRequest.Files.Count < 1)
            {
                throw new ArgumentNullException("No files were selected to upload.");
            }
            
            try
            {
                using (var db = GetContext())
                {
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }

                    var responseObject = db.FileUpload(httpRequest, @"~/assets/enclosures/" + path + '/');

                    return responseObject;
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