#ifndef UNITENUM_H
#define UNITENUM_H

#define MILLIMETER  1
#define CENTIMETER  10
#define METER  1000
#define INCH  25.4
#define FOOT  304.8

class enum unitenum {
    public: 
        double convertFromMillimetersToUnit(double distanceInMillimeters, std::string unitName);
        double convertFromUnitToMillimeters(double distanceInUnits, string unitName);
       
    private:
        string unitName;
}

#endif