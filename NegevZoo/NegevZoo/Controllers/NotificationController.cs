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
        /// update the device state.
        /// </summary>
        /// <param name="deviceId">The device id to add</param>
        /// <param name="inPark">a boolean that indicates if the device is in the park</param>
        [HttpGet]
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
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Device Id: " + deviceId + ", in park: " + inPark);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Subscribes a device to get noficiations.
        /// </summary>
        /// <param name="deviceId">The device's id.</param>
        [HttpGet]
        [Route("notification/{deviceId}/subscribe")]
        public void SubscribeDevice(string deviceId)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.SubscribeDevice(deviceId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Device Id: " + deviceId);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Unsubscribes a device from getting notifications.
        /// </summary>
        /// <param name="deviceId">The device id.</param>
        [HttpGet]
        [Route("notification/{deviceId}/unsubscribe")]
        public void UnsubscribeDevice(string deviceId)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UnsubscribeDevice(deviceId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Device Id: " + deviceId);
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
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Title: " + title + ", body: " + body);
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
            catch(Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace, "Title: " + title + ", body: " + body);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// send notification to online devices about RecurringEvents.
        /// </summary>
        /// <returns>The result of the operation.</returns>
        [HttpGet]
        [Route("notification/online/recurring")]
        public IHttpActionResult SendNotificationsOnlineRecurringEvents()
        {
            try
            {
                using (var db = GetContext())
                {
                    db.SendNotificationsOnlineDevicesRecurringEvents();
                }

                return Ok();
            }
            catch (Exception Exp)
            {
                Logger.GetInstance(isTesting).WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
    }
}
