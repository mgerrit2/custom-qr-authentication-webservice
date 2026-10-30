# Sample to implement a custom QR Authentication

# Gradle dependencies

``` groovy
    configurations.all {
        resolutionStrategy.eachDependency { details ->
            if (details.requested.group == 'com.beust' && details.requested.name == 'jcommander') {
                details.useVersion '1.78'
                details.because('Mitigates WS-2019-0490 security vulnerability')
            }
            if (details.requested.group.startsWith('com.fasterxml.jackson')) {
                if (details.requested.name == 'jackson-annotations') {
                    details.useVersion '2.21'
                } else {
                    details.useVersion '2.21.8'
                }
                details.because('Mitigates CVE-2026-110879 and aligns all Jackson modules to 2.21.8')
            }
        }
    }

	// two way authentication
	implementation 'dev.samstevens.totp:totp-spring-boot-starter:1.7.1'

	implementation 'io.jsonwebtoken:jjwt-api:0.13.0'
	implementation 'io.jsonwebtoken:jjwt-impl:0.13.0'
	implementation 'io.jsonwebtoken:jjwt-jackson:0.13.0'
```

