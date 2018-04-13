using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL
{
    public class Logger
    {
        private static Logger logger;
        private static String filePath;

        protected Logger()
        {
            filePath = String.Format(@"logs\{0}.log", DateTime.Now.ToString("yyyy-dd-M--HH-mm-ss"));
        }

        public static Logger GetInstance()
        {
            if (logger == null)
            {
                logger = new Logger();
            }

            return logger;
        }

        public String ReadLine()
        {
            try
            {
                var stream = File.Open(filePath, FileMode.Open, FileAccess.Read);

                StreamReader reader = new StreamReader(stream);
                var line = reader.ReadLine();
                reader.Dispose();
                return line;
            }
            catch
            {
                return null;
            }
        }

        public void WriteLine(String message/*Actions action, params string[] options*/)
        {
            try
            {
                FileStream stream = null;

                if (!File.Exists(filePath))
                {
                    stream = File.Create(filePath);
                }
                else
                {
                    stream = File.Open(filePath, FileMode.Append, FileAccess.Write);
                }

                StreamWriter writer = new StreamWriter(stream);
                writer.WriteLine(message);
                writer.Dispose();
                stream.Dispose();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }

        }
    }
}
