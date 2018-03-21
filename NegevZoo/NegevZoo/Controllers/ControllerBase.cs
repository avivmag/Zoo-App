using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
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
    }
}
