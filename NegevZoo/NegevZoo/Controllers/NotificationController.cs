using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web.Http;

namespace NegevZoo.Controllers
{
    public class NotificationController : ControllerBase
    {

        /// <summary>
        /// update on an online user.
        /// </summary>
        /// <param name="deviceId">The device id to add</param>
        /// [HttpGet]
        [Route("notification/updateDevice/{deviceId}/{longtitude}/{latitude}")]
        public void UpdateDeviceOnline(string deviceId, Double longtitude, Double latitude)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateDevice(deviceId, longtitude, latitude);
                }

            }
            catch (Exception Exp)
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        [HttpGet]
        [Route("notification/send/{title}/{body}")]
        public IHttpActionResult SendNotifications(string title, string body)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.SendNotificationsAllDevices(title, body);
                }

                return Ok();
            }
            catch(Exception exp)
            {
                // TODO:: LOG.
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
    }
}
