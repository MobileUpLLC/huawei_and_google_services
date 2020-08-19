# Huawei and Google services example

This repository shows how to use Huawei or Google services for Maps, Location and Analytics in different flavors.

### Preparations

You need to create accounts in Huawei and Google, enable services in developer console and get config files and keys.

#### Huawei

1. Create developer account.
2. Create application, define application id (package).
3. Enable Analytics in application.
4. Provide `SHA-256` of signing keys.
5. Download `agconnect-services.json` and put in in `app/src/huawei/`
6. If you use Huawei device for tests - make sure, that `Huawei Mobile Services` updated to latest version.
7. If you use non Huawei device - install `Huawei Mobile Services` APK from `Huawei App Gallery`. Provide system permissions for it. In that case you could use analytics feature and location feature, but maps won't work.

#### Google

1. Create Google and Firebase accounts.
2. Create project in Firebase.
3. Add android application in Firebase. Provide `SHA-1` of signing keys.
4. Download `google-services.json` and put in in `app/`
5. Go to Google Cloud Console, find you project, created automatically in step 2.
6. Find key for MapKit. Put in instead of `PROVIDE HERE YOU GOOGLE API KEY FOR MAPS FROM GOOGLE CLOUD CONSOLE` in `app/src/google/AndroidManifest.xml`
7. Make sure, that you have `Google Mobile Services` installed and enabled on you device.

### Launch

Choose one of flavor `huawei` or `google` and see how it works!

Huawei and Google maps with markers:

![Huawei maps with markers](/img/huawei-maps.png)
![Google maps with markers](/img/google-maps.png)