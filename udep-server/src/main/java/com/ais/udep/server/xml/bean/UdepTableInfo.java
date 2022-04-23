package com.ais.udep.server.xml.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

@Data
@XmlAccessorType(XmlAccessType.NONE)
public class UdepTableInfo implements Serializable {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String opCode;
    @XmlAttribute
    private String dbName;
    @XmlValue
    private String value;

    public String getKey(){
        // 以&作为分隔符
        return this.getId()+"&"+this.getOpCode()+"&"+this.getDbName();
    }
}
