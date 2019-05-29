package org.troparo.services.loanservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-28T16:50:13.407+02:00
 * Generated source version: 3.2.7
 *
 */
@WebService(targetNamespace = "http://troparo.org/services/LoanService/", name = "ILoanService")
@XmlSeeAlso({org.troparo.entities.loan.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ILoanService {

    @WebMethod
    @WebResult(name = "getLoanByIdResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.GetLoanByIdResponseType getLoanById(
        @WebParam(partName = "parameters", name = "getLoanByIdRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.GetLoanByIdRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "getLoanByCriteriasResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.GetLoanByCriteriasResponseType getLoanByCriterias(
        @WebParam(partName = "parameters", name = "getLoanByCriteriasRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.GetLoanByCriteriasRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "getLoanStatusResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.GetLoanStatusResponseType getLoanStatus(
        @WebParam(partName = "parameters", name = "getLoanStatusRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.GetLoanStatusRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "LoanListResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.LoanListResponseType getAllLoans(
        @WebParam(partName = "parameters", name = "LoanListRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.LoanListRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "isRenewableResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.IsRenewableResponseType isRenewable(
        @WebParam(partName = "parameters", name = "isRenewableRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.IsRenewableRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "renewLoanResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.RenewLoanResponseType renewLoan(
        @WebParam(partName = "parameters", name = "renewLoanRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.RenewLoanRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "addLoanResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.AddLoanResponseType addLoan(
        @WebParam(partName = "parameters", name = "addLoanRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.AddLoanRequestType parameters
    ) throws BusinessExceptionLoan;

    @WebMethod
    @WebResult(name = "terminateLoanResponseType", targetNamespace = "http://troparo.org/entities/loan", partName = "parameters")
    public org.troparo.entities.loan.TerminateLoanResponseType terminateLoan(
        @WebParam(partName = "parameters", name = "terminateLoanRequestType", targetNamespace = "http://troparo.org/entities/loan")
        org.troparo.entities.loan.TerminateLoanRequestType parameters
    ) throws BusinessExceptionLoan;
}
