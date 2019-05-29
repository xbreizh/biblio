package org.troparo.services.memberservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-28T16:50:13.262+02:00
 * Generated source version: 3.2.7
 *
 */
@WebService(targetNamespace = "http://troparo.org/services/MemberService/", name = "IMemberService")
@XmlSeeAlso({org.troparo.entities.member.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IMemberService {

    @WebMethod
    @WebResult(name = "MemberListResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.MemberListResponseType getAllMembers(
        @WebParam(partName = "parameters", name = "MemberListRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.MemberListRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "removeMemberResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.RemoveMemberResponseType removeMember(
        @WebParam(partName = "parameters", name = "removeMemberRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.RemoveMemberRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "addMemberResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.AddMemberResponseType addMember(
        @WebParam(partName = "parameters", name = "addMemberRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.AddMemberRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "updateMemberResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.UpdateMemberResponseType updateMember(
        @WebParam(partName = "parameters", name = "updateMemberRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.UpdateMemberRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "getMemberByIdResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.GetMemberByIdResponseType getMemberById(
        @WebParam(partName = "parameters", name = "getMemberByIdRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.GetMemberByIdRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "getMemberByCriteriasResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.GetMemberByCriteriasResponseType getMemberByCriterias(
        @WebParam(partName = "parameters", name = "getMemberByCriteriasRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.GetMemberByCriteriasRequestType parameters
    ) throws BusinessExceptionMember;

    @WebMethod
    @WebResult(name = "getMemberByLoginResponseType", targetNamespace = "http://troparo.org/entities/member", partName = "parameters")
    public org.troparo.entities.member.GetMemberByLoginResponseType getMemberByLogin(
        @WebParam(partName = "parameters", name = "getMemberByLoginRequestType", targetNamespace = "http://troparo.org/entities/member")
        org.troparo.entities.member.GetMemberByLoginRequestType parameters
    ) throws BusinessExceptionMember;
}
