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
        private static DateTime date;

        protected Logger()
        {
            date        = DateTime.Today;
            filePath    = String.Format(@"c:\Zoo-Logs\{0}.log", date.ToString("yyyy-MM-dd"));
        }

        public static Logger GetInstance()
        {
            if (logger == null || date != DateTime.Today)
            {
                logger      = new Logger();
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

        public void WriteLine(params String[] messages)
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
                writer.WriteLine(DateTime.Now);
                foreach (String message in messages)
                {
                    writer.WriteLine(message);
                }
                writer.WriteLine();
                writer.WriteLine();
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
