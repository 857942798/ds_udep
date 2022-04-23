package com.ais.udep.server.xml.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@Data
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class UdepRoot implements Serializable {
    @XmlElement(name = "tableInfo")
    public List<UdepTableInfo> tableInfoList;

}
