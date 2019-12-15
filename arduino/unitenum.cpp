#include "unitenum.h"
#include <string>

using namespace std;

//unitName is desired unit to convert millimeters into.
double unitenum :: convertFromMillimetersToUnit(double distanceInMillimeters, std::string unitName) {
    switch(unitName) {
        case "millimeters":
            return distanceInMillimeters;
            break;
        case "centimeters":
            return distanceInMillimeters / CENTIMETER;
            break;
        case "inches":
            return distanceInMillimeters / INCH;
            break;
        case "feet":
            return distanceInMillimeters / FOOT;
            break;
        case "meters":
            return distanceInMillimeters / METER;
            break;
    }
}

//unitName is the name of the type of units distanceInUnit is.
double unitenum :: convertFromUnitToMillimeters(double distanceInUnit, string unitName) {
     switch(unitName) {
        case "millimeters":
            return distanceInUnit;
            break;
        case "centimeters":
            return distanceInUnit * CENTIMETER;
            break;
        case "inches":
            return distanceInUnit * INCH;
            break;
        case "feet":
            return distanceInUnit * FOOT;
            break;
        case "meters":
            return distanceInUnit * METER;
            break;
    }
}