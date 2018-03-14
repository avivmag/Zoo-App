using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Web.Http;
using System.Web.Http.Cors;

namespace NegevZoo
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            var corsConfig = new EnableCorsAttribute(
                "*",
                string.Join(
                    ",",
                    "Accept",
                    "Accept-Encoding",
                    "Authorization",
                    "Origin",
                    "Content-Type",
                    "X-Csrf-Token"),
                string.Join(
                    ",",
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE"))
            {
                SupportsCredentials = true
            };
            config.EnableCors(corsConfig);
            config.MapHttpAttributeRoutes();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
            config.Formatters.JsonFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("text/html"));
        }
    }
}
