#cavespring#
**Simple Tool for Amazon Route53**

---

CaveSpring is a Java based library to let you access Amazon's Route53 service from your Java code. It was written well before Amazon released an API. It works beautifully.

I have also added command line interface, in case you wanted to get stuffs done quickly.

###Usage###
Using CaveSpring is simple two step procecess:

1. **Setup access and secret keys:** Place your EC2 Access Key and EC2 Secret Key at appropriate place in `$CAVESPRING_HOME/conf/config.properties` like this

        EC2_ACCESS_KEY = XXXXXXXXXXXXXXX
        EC2_SECRET_KEY = YYYYYYYYYYYYYYY
If you do not do this, you might see `java.lang.IllegalArgumentException: Empty key`
2. **Start using commands:** `$CAVESPRING_HOME/bin` has scripts compatible to Unix and Windows platforms. You can run them with appropriate parameters. To know the usage refer the [wiki](https://github.com/naishe/cavespring/wiki) or you can see usage by running scripts with `-h` or `--help` option.


###What's CaveSpring?###
If you see wikipedia [Georgia State Route 53](http://en.wikipedia.org/wiki/Georgia_State_Route_53)'s west end is Cave Spring. So, I named this project this way.

###Contact me###
I would like to help you as much as I can. If you wanted to improve the project or have any suggestion, you're welcome. My email address is nishant[DOT]neeraj[AT]gmail[DOT]com
