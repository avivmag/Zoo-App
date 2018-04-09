using DAL;
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
        /// get all the devices.
        /// </summary>
        /// <returns>all the devices.</returns>
        [HttpGet]
        [Route("notification/all")]
        public IEnumerable<Device> GetAllDevices()
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllDevices();
                }

            }
            catch (Exception Exp)
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// update on an online user.
        /// </summary>
        /// <param name="deviceId">The device id to add</param>
        /// <param name="inPark">a boolean that indicates if the device is in the park</param>
        [HttpPost]
        [Route("notification/updateDevice/{deviceId}/{inPark}")]
        public void UpdateDevice(string deviceId, bool inPark)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateDevice(deviceId, inPark);
                }

            }
            catch (Exception Exp)
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// send notification to all devices.
        /// </summary>
        /// <param name="title">the title of the notification</param>
        /// <param name="body">the body of the notification</param>
        /// <returns>The result of the operation.</returns>
        [HttpGet]
        [Route("notification/all/{title}/{body}")]
        public IHttpActionResult SendNotificationsAllDevices(string title, string body)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.SendNotificationsAllDevices(title, body);
                }

                return Ok();
            }
            catch (Exception exp)
            {
                // TODO:: LOG.
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// send notification to online devices.
        /// </summary>
        /// <param name="title">the title of the notification</param>
        /// <param name="body">the body of the notification</param>
        /// <returns>The result of the operation.</returns>
        [HttpGet]
        [Route("notification/online/{title}/{body}")]
        public IHttpActionResult SendNotificationsOnline(string title, string body)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.SendNotificationsOnlineDevices(title, body);
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
