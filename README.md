# OWASP juice-shop assessment
Presentation and code from OWASP AppSec DC 2019 - "Testing With Your Left Foot Forward"

# Prerequisites

You will need chromedriver installed:

```bash
$ brew cask upgrade chromedriver
```

You will need to update the attributes so it can be called:

```bash
$ which chromedriver
/usr/local/bin/chromedriver
xattr -d com.apple.quarantine  /usr/local/bin/chromedriver 
```

# Running the Tests

In order for the test cases in this repo to work you must have the [OWASP Juice Shop](https://github.com/bkimminich/juice-shop)
running on your localhost:

```
docker run -d -p 3000:3000 bkimminich/juice-shop
```

Some of the test cases use BrowserMobProxy - which does not proxy `localhost` so some of the tests are routed
to `http://kubernetes.docker.internal:3000` (which will be the running Juice Shop on most Docker installations).

To actually run the tests execute:

```
./gradlew integrationTest
```

### Happy Testing!
