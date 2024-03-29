package cn.edu.cqupt.Contract;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Auto generated code.
 * <p>
 * <strong>Do not modify!</strong>
 * <p>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j
 * command line tools</a>, or the
 * org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen
 * module</a> to update.
 *
 * <p>
 * Generated with web3j version 3.4.0.
 */
public class ERC20Interface extends Contract {
	private static final String BINARY = "";

	public static final String FUNC_APPROVE = "approve";

	public static final String FUNC_TOTALSUPPLY = "totalSupply";

	public static final String FUNC_TRANSFERFROM = "transferFrom";

	public static final String FUNC_BALANCEOF = "balanceOf";

	public static final String FUNC_TRANSFER = "transfer";

	public static final String FUNC_ALLOWANCE = "allowance";

	public static final Event TRANSFER_EVENT = new Event("Transfer",
			Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
			}, new TypeReference<Address>() {
			}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
			}));;

	public static final Event APPROVAL_EVENT = new Event("Approval",
			Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
			}, new TypeReference<Address>() {
			}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
			}));;

	public ERC20Interface(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
						  BigInteger gasLimit) {
		super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
	}

	protected ERC20Interface(String contractAddress, Web3j web3j, TransactionManager transactionManager,
                             BigInteger gasPrice, BigInteger gasLimit) {
		super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
	}

	public RemoteCall<TransactionReceipt> approve(String spender, BigInteger tokens) {
		final Function function = new Function(FUNC_APPROVE,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(spender),
						new org.web3j.abi.datatypes.generated.Uint256(tokens)),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	public RemoteCall<BigInteger> totalSupply() {
		final Function function = new Function(FUNC_TOTALSUPPLY, Arrays.<Type>asList(),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));
		return executeRemoteCallSingleValueReturn(function, BigInteger.class);
	}

	public RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokens) {
		final Function function = new Function(FUNC_TRANSFERFROM,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), new org.web3j.abi.datatypes.Address(to),
						new org.web3j.abi.datatypes.generated.Uint256(tokens)),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	public RemoteCall<BigInteger> balanceOf(String tokenOwner) {
		final Function function = new Function(FUNC_BALANCEOF,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(tokenOwner)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));
		return executeRemoteCallSingleValueReturn(function, BigInteger.class);
	}

	public RemoteCall<TransactionReceipt> transfer(String to, BigInteger tokens) {
		final Function function = new Function(FUNC_TRANSFER,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to),
						new org.web3j.abi.datatypes.generated.Uint256(tokens)),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	public RemoteCall<BigInteger> allowance(String tokenOwner, String spender) {
		final Function function = new Function(FUNC_ALLOWANCE,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(tokenOwner),
						new org.web3j.abi.datatypes.Address(spender)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));
		return executeRemoteCallSingleValueReturn(function, BigInteger.class);
	}

	public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
		List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
		ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
		for (Contract.EventValuesWithLog eventValues : valueList) {
			TransferEventResponse typedResponse = new TransferEventResponse();
			typedResponse.log = eventValues.getLog();
			typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
			typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
			typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
			responses.add(typedResponse);
		}
		return responses;
	}

	public Observable<TransferEventResponse> transferEventObservable(EthFilter filter) {
		return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
			@Override
			public TransferEventResponse call(Log log) {
				Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
				TransferEventResponse typedResponse = new TransferEventResponse();
				typedResponse.log = log;
				typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
				typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
				typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
				return typedResponse;
			}
		});
	}

	public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock,
                                                                     DefaultBlockParameter endBlock) {
		EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
		filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
		return transferEventObservable(filter);
	}

	public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
		List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
		ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
		for (Contract.EventValuesWithLog eventValues : valueList) {
			ApprovalEventResponse typedResponse = new ApprovalEventResponse();
			typedResponse.log = eventValues.getLog();
			typedResponse.tokenOwner = (String) eventValues.getIndexedValues().get(0).getValue();
			typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
			typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
			responses.add(typedResponse);
		}
		return responses;
	}

	public Observable<ApprovalEventResponse> approvalEventObservable(EthFilter filter) {
		return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
			@Override
			public ApprovalEventResponse call(Log log) {
				Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
				ApprovalEventResponse typedResponse = new ApprovalEventResponse();
				typedResponse.log = log;
				typedResponse.tokenOwner = (String) eventValues.getIndexedValues().get(0).getValue();
				typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
				typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
				return typedResponse;
			}
		});
	}

	public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock,
                                                                     DefaultBlockParameter endBlock) {
		EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
		filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
		return approvalEventObservable(filter);
	}

	public static RemoteCall<ERC20Interface> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
                                                    BigInteger gasLimit) {
		return deployRemoteCall(ERC20Interface.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
	}

	public static RemoteCall<ERC20Interface> deploy(Web3j web3j, TransactionManager transactionManager,
                                                    BigInteger gasPrice, BigInteger gasLimit) {
		return deployRemoteCall(ERC20Interface.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
	}

	public static ERC20Interface load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
                                      BigInteger gasLimit) {
		return new ERC20Interface(contractAddress, web3j, credentials, gasPrice, gasLimit);
	}

	public static ERC20Interface load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
                                      BigInteger gasPrice, BigInteger gasLimit) {
		return new ERC20Interface(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
	}

	public static class TransferEventResponse {
		public Log log;

		public String from;

		public String to;

		public BigInteger tokens;
	}

	public static class ApprovalEventResponse {
		public Log log;

		public String tokenOwner;

		public String spender;

		public BigInteger tokens;
	}
}
