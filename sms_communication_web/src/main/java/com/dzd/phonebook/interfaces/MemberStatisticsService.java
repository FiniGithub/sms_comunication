package com.dzd.phonebook.interfaces;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path(value = "/V1/memberService")
public interface MemberStatisticsService {
	
    @GET
    @Path("/updateMemberStatistics")
    @Consumes(MediaType.APPLICATION_JSON)
    public String regedit(String json) throws IOException;

}
