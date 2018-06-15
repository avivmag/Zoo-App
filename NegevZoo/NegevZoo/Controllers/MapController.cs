using DAL;
using DAL.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Authentication;
using System.Web;
using System.Web.Http;

namespace NegevZoo.Controllers
{
    public class MapController : ControllerBase
    {
        #region File Upload

        /// <summary>
        /// Uploads a new map image.
        /// </summary>
        [HttpPost]
        [Route("map/upload")]
        public void UploadMap()
        {
            try
            {
                var httpRequest = HttpContext.Current.Request;
                if (httpRequest.Files.Count < 1)
                {
                    throw new ArgumentNullException("No map file was given.");    
                }

                using (var db = GetContext())
                {
                    db.MapUpload(httpRequest);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Updates the map parameters.
        /// </summary>
        /// <param name="coords">The coords to update the map by.</param>
        [HttpPost]
        [Route("map/coords/upload")]
        public void UploadCoords(MapCoords coords)
        {
            try
            {
                using (var db = GetContext())
                {
                    if (ValidateSessionId(db))
                    {
                        db.InitMapSettings(coords);
                    }
                    else
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }
                }
            }
            catch (Exception Exp)
            {
                string mapSettingsInput =
                    "point1Longitude: " + coords.FirstLongitude + ", " +
                    "point1Latitude: " + coords.FirstLatitude + ", " +
                    "point1XLocation: " + coords.FirstX + ", " +
                    "point1YLocation: " + coords.FirstY + ", " +
                    "point2Longitude: " + coords.SecondLongitude + ", " +
                    "point2Latitude: " + coords.SecondLatitude + ", " +
                    "point2XLocation: " + coords.SecondX + ", " +
                    "point2YLocation: " + coords.SecondY;

                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Map settings: " + mapSettingsInput);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #region Map Settings

        /// <summary>
        /// Gets the map settings
        /// This method use for the visitore app to get all the settings of the map.
        /// </summary>
        /// <returns>MapSettingsResult object which contains all the setting result.</returns>
        [HttpGet]
        [Route("map/settings")]
        public MapSettingsResult GetMapSettings()
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetMapSettings();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Uploads a new misc image.
        /// </summary>
        /// <returns>Whether the upload succeeded.</returns>
        [HttpPost]
        [Route("map/misc/upload")]
        public IHttpActionResult MapImagesUpload()
        {
            var httpRequest = HttpContext.Current.Request;
            if (httpRequest.Files.Count < 1)
            {
                return BadRequest();
            }

            try
            {
                using (var db = GetContext())
                {
                    if (!ValidateSessionId(db))
                    {
                        throw new AuthenticationException("Couldn't validate the session");
                    }

                    var responseObject = db.FileUpload(httpRequest, @"~/assets/map/misc/");

                    return Ok(responseObject);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        #endregion

        #region Markers

        /// <summary>
        /// Returns all markers of the map.
        /// </summary>
        /// <returns>All markers (enclosure and misc).</returns>
        [HttpGet]
        [Route("map/markers")]
        public IEnumerable<MiscMarker> GetAllMarkers()
        {
            try
            {
                using (var db = this.GetContext())
                {
                    var allMarkers = db.GetAllMarkers();

                    return allMarkers;
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Returns all misc markers of the map.
        /// </summary>
        /// <returns>All misc markers of the map.</returns>
        [HttpGet]
        [Route("map/markers/misc")]
        public IEnumerable<MiscMarker> GetMiscMarkers()
        {
            try
            {
                using (var db = this.GetContext())
                {
                    var miscMarkers = db.GetMiscMarkers();

                    return miscMarkers;
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Adds or updates a misc marker.
        /// </summary>
        /// <param name="marker">The marker to update.</param>
        [HttpPost]
        [Route("map/markers/misc/update")]
        public void UpdateMiscMarker(MiscMarker marker)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateMiscMarker(marker);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        [HttpDelete]
        [Route("map/markers/{markerId}/delete")]
        public void DeleteMiscMarker(int markerId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteMiscMarker(markerId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        #endregion
    }
}
