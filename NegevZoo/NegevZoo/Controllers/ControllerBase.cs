using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using DAL;

namespace NegevZoo.Controllers
{
    public class ControllerBase : ApiController
    {
        static bool isTesting = true;

        public ZooContext GetContext()
        {
            return new ZooContext(isTesting);
        }
    }
}
