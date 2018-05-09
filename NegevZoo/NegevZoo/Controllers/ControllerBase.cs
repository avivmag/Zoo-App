using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web.Http;
using BL;

namespace NegevZoo.Controllers
{
    public class ControllerBase : ApiController
    {
        public static bool isTesting = false;

        /// <summary>
        /// Gets the zoo Context.
        /// </summary>
        /// <returns>The zoo context.</returns>
        public ZooContext GetContext()
        {
            return new ZooContext(isTesting);
        }

        public bool ValidateSessionId(ZooContext db)
        {
            CookieHeaderValue cookie = Request.Headers.GetCookies("session-id").FirstOrDefault();
            if (cookie != null && cookie["session-Id"].Value != null)
            {
                return db.ValidateSession(cookie["session-id"].Value);
            }
            
            return false;
        }

        public string GetSessionId()
        {
            CookieHeaderValue cookie = Request.Headers.GetCookies("session-id").FirstOrDefault();
            if (cookie != null)
            {
                return cookie["session-id"].Value;
            }

            return null;
        }
    }
}
