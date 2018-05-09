using DAL;
using DAL.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace NegevZoo.Controllers
{
    public class MapController : ControllerBase
    {
        #region Map

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
                    db.UploadMap(httpRequest);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// This method intitiates the map with the given parameters. 
        /// Note: the values inside the CSV file must be seperated with ","
        /// </summary>
        /// <param name="pointsFilePath"> This variables represents the path of the CSV file that contains the points of the map.</param>
        /// <param name="longitude"> This variable represents the longitude of a point in the map</param>
        /// <param name="latitude">This variable represents the latitude of a point in the map</param>
        /// <param name="xLocation"> This variable represents the location of the longitude on the map picture</param>
        /// <param name="yLocation"> This variable represents the location of the latitude on the map picture</param>
        [HttpGet]
        [Route("map/initvars/{pointsFilePath}/{longitude}/{latitude}/{xLocation}/{yLocation}")]
        public void InitMapSettings(string pointsFilePath, double longitude, double latitude, int xLocation, int yLocation)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.InitMapSettings(pointsFilePath, longitude, latitude, xLocation, yLocation);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                    var responseObject = db.FileUpload(httpRequest, @"~/assets/map/misc/");

                    return Ok(responseObject);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
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
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        #endregion
    }
}
