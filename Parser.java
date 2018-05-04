//PROJECT 2: ROHANA SOSA & TRISTAN STILL
//NOTE: Replace content of test.txt with content of Test22.txt, Test33.txt, Test44.txt, and Test55.txt to test programs
// THIS PROGRAM IS PERFECTLY CORRECT - COMPLETED ON MARCH 31ST, 2018
package analyzerPkg;

/* ?????????????????? in the body of a method means you need to change the production rule based on three method 
 *  that I motioned in class: Eliminating left recursion, left factorization and/or elimination of null string.
 *  There are 8 test programs:
 *  Test2.txt, Test3.txt, Test4.txt, and Test5.txt that grammatically are correct.
 *  Test22.txt, Test33.txt, Test44.txt, and Test55.txt that grammatically are NOT correct.
{M} means zero or more Ms.
[M] means zero or one M.
EBNF
Program = "program" ProgramName ";" BlockBody ".".
BlockBody = [ConstantDefinitionPart][TypeDefinitionPart]
            [VariableDefinitionPart] {ProcedureDefinition}
            CompoundStatement.
ConstantDefinitionPart = "const" ConstantDefinition { ConstantDefinition }.
ConstantDefinition = constantName "=" Constant ";".
Type = ArrayType | RecordType | SimpleType.
ArrayType = "array" "[" IndexRange "]" SimpleType.
IndexRange = Constant ".." Constant.
RecordType = "record" FieldList "end".
FieldList = RecordSection { ";" RecordSection }.
RecordSection = FieldName { "," FieldName  } ":" Type.
VariableDefinitionPart = "var" VariableDefinition {VariableDefinition}.
VariableDefinition = VariableGroup ";".
VariableGroup = VariableName  { "," VariableName } ":" Type.
SimpleType = "integer" | "boolean"
ProcedureDefinition = "procedure" ProcedureName ProcedureBlock ";".
ProcedureBlock = [ "(" FormalParameterList ")" ] ";" BlockBody.
FormalParameterList = ParameterDefinition {";" ParameterDefinition }.
ParameterDefinition = ["var"] VariableGroup. 
Statement = AssignmentStatement | ProcedureStatement | IfStatement
          | WhileStatement | CompoundStatement | Empty.

Note for the parser: Because of: Empty we do not write: new Error(...).
AssignmentStatement = VariableAccess ":=" Expression.
Selector = IndexSelector | FieldSelector.
IndexSelector = "[" Expression "]".
FieldSelector = "." FieldName.
ProcedureStatement = ProcedureName [ "(" ActualParameterList ")" ].
ActualParameterList = ActualParameter {"," ActualParameter}.
ActualParameter = Expression | VariableAccess.
VariableAccess = VariableName {Selector}.
ActualParameter = Expression.
CompoundStatement = "{" Statement { ";" Statement } "}". 
WhileStatement = "while" Expression "do" Statement. 
IfStatement = "if" Expression "then" Statement [ "else" Statement ].
Expression = SimpleExpression [ RelationalOperator SimpleExpression ]. 
SimpleExpression = [ SignOperator ] Term { AddingOperator Term }.
RelationalOprator = "<" | "=" | ">" | "<>".
SignOperator = "+" | "-".
Term = Factor { MultiplyOperator Factor }. 
AddingOperator = "+" | "-" | "or".
Factor = Constant | VariableAccess | "(" Expression ")" |"not" Factor. 
MultiplyOperator = "*" | "div" | "mod" | "and".
Constant = Numeral | "false" | "true".
*/

public class Parser {
	private Token currentToken;
	private Lexer lexer;

	private void expect(int expectedToken) {
		if (currentToken.kind == expectedToken)
			currentToken = lexer.scan();
		else
			new Error("Syntax Error: " + currentToken.spelling + " is not expected", currentToken.line);
	}

	private void expectIt() {
		currentToken = lexer.scan();
	}

	public void parse() {
		lexer = new Lexer();
		currentToken = lexer.scan();
		parseProgram();
		if (currentToken.kind != Token.EOT)
			new Error("Syntax error: Redundant characters at the end of program.", currentToken.line);
	}

	// Program = "program" ProgramName ";" BlockBody "."
	private void parseProgram() {
		expect(Token.PROGRAM);
		expect(Token.IDENTIFIER);
		expect(Token.SEMICOLON);
		parseBlockBody();
		expect(Token.PERIOD);
	}

	//1. Write a program with indentation. My Response: Okay, I formatted the indentation.
	
	//2. The symbol: { and } are not terminal symbols. If they where, they where with quotations. That is: "{" and "}"
	//{M} means zero or more of M. M is whatever you have in { and }. My Response: I followed Rule #4 for {Procedure Definition}. Rule #4: Replace a({E}) by: while currentToken is in First(E) do a(E)
	
	//3. You forgot to write parseVariableDefinition. My Response: Okay, I added parseVariableDefinition.
/*
	4. Bases on which rule you wrote these lines? My Response: I thought we needed to look at the grammar at the very top where it says: CompoundStatement = "{" Statement { ";" Statement } "}".

		  expect(Token.LBRACE);
	  	  parseAssignmentOrProcedureStatement();
	      while(currentToken.kind == Token.SEMICOLON)
	      parseAssignmentOrProcedureStatement();
	      expect(Token.RBRACE);
	       
	A) In this production rule is there any: "{"? My response: No.
	
	B) In this production rule is there any none terminal named: Statement? My response: No.
	
	C) In this production rule is there any: ";"? My response: No.
	
	D) In this production rule is there any: "}"? My response: No.

	
*/
	//BlockBody = [ ConstantDefinitionPart ] [ TypeDefinitionPart ] [ VariableDefinitionPart ] { ProcedureDefinition 
	//CompoundStatement

	private void parseBlockBody() {
	  if(currentToken.kind == Token.CONST) 
	    parseConstantDefinitionPart();
	  if(currentToken.kind == Token.TYPE) 
	    parseTypeDefinitionPart();
	  if(currentToken.kind == Token.VAR)
	    parseVariableDefinitionPart(); //Badii 
	  while(currentToken.kind == Token.PROCEDURE) //Bdii: It was: while(currentToken.kind == Token.PROCEDURE); 
	    parseProcedureDefinition();
	  parseCompoundStatement();
	}


	//The following method is correct. But what is " .*)" at the end of the production rule? My Response: I deleted " .*)" at the end because that's a mistake.

	//ConstantDefinitionPart = "const" ConstantDefinition { ConstantDefinition } 

	private void parseConstantDefinitionPart() {
	  expect(Token.CONST);
	  parseConstantDefinition();
	  while(currentToken.kind == Token.IDENTIFIER)
	    parseConstantDefinition();
	} 



	//************* NEXT TWO METHODS BELOW **************



	// ConstantDefinition = ConstantName "=" Constant ";" .
	
	/* Rule 2: Replace a(F1 F2…Fn) by: a(F1); a(F2);…, a(Fn);
	 * Rule 3: Replace a(N), where N is a nonterminal symbol by the call:
	 * Rule 5: Replace a(t), where t is a terminal symbol by the call: accept(t).
	 * F1 = ConstantName 
	 * F2 = "=" 
	 * F3 = Constant 
	 * F4 = ";"
	 */
	
	private void parseConstantDefinition() {
		expect(Token.IDENTIFIER);
		expect(Token.EQUAL);
		parseConstant();
		expect(Token.SEMICOLON);
	}

	// DONE


	// TypeDefinitionPart = "type" TypeDefinition { TypeDefinition } .
	/*
	 * Rule 2: Replace a(F1 F2…Fn) by: a(F1); a(F2);…, a(Fn); 
	 * Rule 3: Replace a(N), where N is a nonterminal symbol by the call: parseN(). 
	 * Rule 4: Replace a({E}) by: while currentToken is in First(E) do a(E) 
	 * Rule 5: Replace a(t), where t is a terminal symbol by the call: accept(t). 
	 * F1 = "type" 
	 * F2 = TypeDefinition 
	 * F3 = { TypeDefinition }
	 */
	// TypeDefinition = TypeName "=" NewType ";" .

	private void parseTypeDefinitionPart() {
		expect(Token.TYPE);
		parseTypeDefinition();
		while (currentToken.kind == Token.IDENTIFIER)
			parseTypeDefinition();
	}

	// DONE



	// ************* NEXT THREE METHODS BELOW **************



	// TypeDefinition = TypeName "=" NewType ";" .
	private void parseTypeDefinition() {
		expect(Token.IDENTIFIER);
		expect(Token.EQUAL);
		parseNewType();
		expect(Token.SEMICOLON);
	}
	// DONE



	// NewType = NewArrayType | NewRecordType .
	private void parseNewType() {
		if (currentToken.kind == Token.ARRAY)
			parseNewArrayType();
		else if (currentToken.kind == Token.RECORD)
			parseNewRecordType();
		else
			new Error("Syntax error: array or record is expected.", currentToken.line);
	}
	// DONE




	// Constant = Numeral | ConstantName.
	private void parseConstant() {
		if (currentToken.kind == Token.INT_VALUE)
			expect(Token.INT_VALUE);
		else if (currentToken.kind == Token.IDENTIFIER)
			expect(Token.IDENTIFIER);
		else
			new Error("Syntax error: numeral or consant is expected.", currentToken.line);
	}
	// DONE




	// ************* NEXT THREE METHODS BELOW **************



	// NewArrayType = "array" "[" IndexRange "]" "of" TypeName .
	private void parseNewArrayType() {
		expect(Token.ARRAY);
		expect(Token.LBRACKET);
		parseIndexRange();
		expect(Token.RBRACKET);
		expect(Token.OF);
		expect(Token.IDENTIFIER);
	}
	// DONE

	// IndexRange = Constant ".." Constant .
	private void parseIndexRange() {
		parseConstant();
		expect(Token.RANGE);
		parseConstant();
	}
	// DONE

	// NewRecordType = "record" FieldList "end" .
	private void parseNewRecordType() {
		expect(Token.RECORD);
		parseFieldList();
		expect(Token.END);
	}
	// DONE

	// ************* NEXT THREE METHODS BELOW **************

	// FieldList = RecordSection { ";" RecordSection } .
	private void parseFieldList() {
		parseRecordSection();
		while(currentToken.kind == Token.SEMICOLON)
		{
			expectIt(); //Badii
			parseRecordSection();
		}
	}
	// DONE

	// RecordSection = FieldName { "," FieldName } ":" TypeName . 
	private void parseRecordSection() {
		expect(Token.IDENTIFIER);
		while (currentToken.kind == Token.COMMA) {
			expect(Token.COMMA);
			expect(Token.IDENTIFIER);
		}
		expect(Token.COLON);
		expect(Token.IDENTIFIER);
	}
	// DONE


	// VariableDefinitionPar = "var" VariableDefinition { VariableDefinition} .
	private void parseVariableDefinitionPart() {
		expect(Token.VAR);
		parseVariableDefinition();
		while (currentToken.kind == Token.IDENTIFIER)
			parseVariableDefinition();//Badii
			//expect(Token.IDENTIFIER);
	}
	// DONE



	// ************* NEXT THREE METHODS BELOW **************



	// VariableDefinition = VariableGroup ";" .
  void parseVariableDefinition() {
		parseVariableGroup();
		expect(Token.SEMICOLON);
	}
	// DONE
  
  // VariableGroup = VariableName  { "," VariableName } ":" TypeName .
  private void parseVariableGroup() {
		expect(Token.IDENTIFIER);
		while (currentToken.kind == Token.COMMA){
			expect(Token.COMMA);
			expect(Token.IDENTIFIER);
		}
		expect(Token.COLON);
		expect(Token.IDENTIFIER);
	}
	// DONE

  // ProcedureDefinition = "procedure" ProcedureName ProcedureBlock ";" .
  void parseProcedureDefinition() {
		expect(Token.PROCEDURE);
		expect(Token.IDENTIFIER);
		parseProcedureBlock();
		expect(Token.SEMICOLON);
	}
	// DONE

	

  // ************* NEXT THREE METHODS BELOW **************



  // ProcedureBlock = [ "(" FormalParameterList ")" ] ";" BlockBody.
  private void parseProcedureBlock() {
		if (currentToken.kind == Token.LPAREN){
			expect(Token.LPAREN);
			parseFormalParameterList();
			expect(Token.RPAREN);
		}
		expect(Token.SEMICOLON);
		parseBlockBody();
	}
	// DONE
  
  // FormalParameterList = ParameterDefinition { ";" ParameterDefinition }.
  private void parseFormalParameterList() {
		parseParameterDefinition();
		while (currentToken.kind == Token.SEMICOLON){
			expect(Token.SEMICOLON);
			parseParameterDefinition();
		}
	}
	// DONE
  
  // ParameterDefinition = ["var"] VariableGroup 
  private void parseParameterDefinition() {
		if (currentToken.kind == Token.VAR)
			expect(Token.VAR);
		parseVariableGroup();
	}
	// DONE


// ************* NEXT THREE METHODS BELOW **************
//Badii
  //Statement = AssignmentStatement | ProcedureStatement | IfStatement
    //      | WhileStatement | CompoundStatement | Empty.

  private void parseStatement()
  {
	  if(currentToken.kind == Token.IDENTIFIER)
		  parseAssignmentOrProcedureStatement();
	  else if(currentToken.kind == Token.IF)
		  parseIfStatement();
	  else if(currentToken.kind == Token.WHILE)
		  parseWhileStatement();
	  else
		  parseCompoundStatement();
  }
  
  // CompoundStatement = "begin" Statement { ";" Statement } "end".
	  private void parseCompoundStatement() {
			expect(Token.BEGIN);
			//Badii parseAssignmentOrProcedureStatement();//Bdii
			parseStatement();
			while (currentToken.kind == Token.SEMICOLON){
				expect(Token.SEMICOLON);
				//Badii parseAssignmentOrProcedureStatement();
				parseStatement();
			}
			expect(Token.END);
		}
		// DONE



	//AssignmentOrProcedureStatement = Identifier AssignmentOrProcedureStatementTail.
	 	private void parseAssignmentOrProcedureStatement() {
			expect(Token.IDENTIFIER);
			parseAssignmentOrProcedureStatementTail();
		}
		// DONE
	 	//VariableAccess = VariableName {Selector}.
	 	//AssignmentOrProcedureStatementTail = {Selector} ":=" Expression |    [ "(" ActualParameterList ")"].
	 	//Note: Since we have Empty we do not write else new Error(...)
	 	//ActualParameterList = ActualParameter {"," ActualParameter}.
	 	private void parseAssignmentOrProcedureStatementTail(){
			if(currentToken.kind == Token.LBRACKET || currentToken.kind == Token.PERIOD || currentToken.kind == Token.ASSIGN){
				
				while(currentToken.kind == Token.LBRACKET || currentToken.kind == Token.PERIOD)
					parseSelector();
				
				expect(Token.ASSIGN);
				
				parseExpression();
			}
			else if(currentToken.kind == Token.LPAREN){
				
				expect(Token.LPAREN);
				parseActualParameterList();
				expect(Token.RPAREN);
			}
			
		}
  
    
  // Selector = IndexSelector | FieldSelector.
	// Rule 7: Replace a(T1 | T2 | ... | Tn)
	//IndexSelector = "[" Expression "]".
	//FieldSelector = "." FieldName.
  private void parseSelector() {
		if (currentToken.kind == Token.LBRACKET)
			parseIndexSelector();
		else if (currentToken.kind == Token.PERIOD)
			parseFieldSelector();
		else
			new Error("Syntax error: IndexSelector or FieldSelector is expected.", currentToken.line);
	}
	// DONE
  
  // IndexSelector = "[" Expression "]".
  private void parseIndexSelector() {
		expect(Token.LBRACKET);
		parseExpression();
		expect(Token.RBRACKET);
	}
	// DONE
  


// ************* NEXT THREE METHODS BELOW **************



  // FieldSelector = "." FieldName.
  private void parseFieldSelector() {
		expect(Token.PERIOD);
		expect(Token.IDENTIFIER);
	}
	// DONE
  // Statement = AssignmentStatement | ProcedureStatement | IfStatement
  //| WhileStatement | CompoundStatement | Empty.
  // WhileStatement = "while" Expression "do" Statement.
  private void parseWhileStatement() {
		expect(Token.WHILE);
		parseExpression();
		expect(Token.DO);
		parseAssignmentOrProcedureStatement();
	}
	// DONE

  // IfStatement = "if" "Expression "then" Statement [ "else" Statement ].
  private void parseIfStatement() {
		expect(Token.IF);
		parseExpression();
		expect(Token.THEN);
		parseStatement();
		if (currentToken.kind == Token.ELSE){
			expect(Token.ELSE);
			parseStatement();
		}
	}
	// DONE



// ************* NEXT THREE METHODS BELOW **************



  //ActualParameterList = ActualParameter {"," ActualParameter}.
  private void parseActualParameterList() {
		parseActualParameter();
		while (currentToken.kind == Token.COMMA){
			expect(Token.COMMA);
			parseActualParameter();
		}		
	}
	// DONE
 
  //  ActualParameter = Expression | VariableAccess .
  private void parseActualParameter() {
		parseExpression();
			
	}
	// DONE

  // Expression = SimpleExpression [ RelationalOperator SimpleExpression ].
  private void parseExpression() {
		parseSimpleExpression();
		if(currentToken.kind == Token.LESS || currentToken.kind == Token.EQUAL || currentToken.kind == Token.GREATER || currentToken.kind == Token.NOTEQUAL)
		{
		parseRelationalOperator();
		parseSimpleExpression();
		}
		
		}	
	// DONE
  

// ************* NEXT THREE METHODS BELOW **************


  // SimpleExpression = [ SignOperator ] Term { AddingOperator Term }.
    private void parseSimpleExpression() {
	if(currentToken.kind == Token.MINUS || currentToken.kind == Token.PLUS)
	parseSignOperator();
	parseTerm();
	while(currentToken.kind == Token.PLUS || currentToken.kind == Token.MINUS || currentToken.kind == Token.OR)
	{//Badii: Missing {
		parseAddingOperator();
	    parseTerm();
	}
}
    //AddingOperator = "+" | "-" | "or".
    private void parseAddingOperator()
    {
    	if(currentToken.kind == Token.PLUS)
    		expect(Token.PLUS);
    	else if(currentToken.kind == Token.MINUS)
    		expect(Token.MINUS);
    	else if(currentToken.kind == Token.OR)
    		expect(Token.OR);
    	else new Error("Syntax error: + or - or OR is expected",currentToken.line);
    }
    //SignOperator = "+" | "-".
    private void parseSignOperator()
    {
    	if(currentToken.kind == Token.PLUS)
    		expect(Token.PLUS);
    	else if(currentToken.kind == Token.MINUS)
    		expect(Token.MINUS);
    	else new Error("Syntax error: + or - is expected",currentToken.line);
    }
	// DONE

  // Term = Factor { MultiplyOperator Factor }.
    
    //Factor = Constant | VariableAccess | "(" Expression ")" |"not" Factor
  private void parseTerm() {
		parseFactor();
		while(currentToken.kind == Token.MULTIPLY ||currentToken.kind == Token.DIV || currentToken.kind == Token.MOD || currentToken.kind == Token.AND)
		{
			parseMultiplyOperator();
			parseFactor();
		}
				
	}
//MultiplyOperator = "*" | "div" | "mod" | "and".
  private void parseMultiplyOperator()
  {
	  if(currentToken.kind == Token.MULTIPLY)
		  expect(Token.MULTIPLY);
	  else if(currentToken.kind == Token.DIV)
		  expect(Token.DIV);
	  else if(currentToken.kind == Token.MOD)
		  expect(Token.MOD);
	  else if(currentToken.kind == Token.AND)
		  expect(Token.AND);
	  else new Error("Syntax error: * or / or MOD or AND is expected",currentToken.line);
  }
	// DONE

  // Factor = Constant | VariableAccess | "(" Expression ")" | "not" Factor.
 // Factor = Numeral | identifier [{Selector}] | "(" Expression ")" | "not" Factor.
  //Constant = Numeral | identifier.
  //Selector = IndexSelector | FieldSelector.
	//	  IndexSelector = "[" Expression "]"
  private void parseFactor() {
		if (currentToken.kind == Token.INT_VALUE)
			expect(Token.INT_VALUE);
		else if (currentToken.kind == Token.IDENTIFIER)
		{
			expect(Token.IDENTIFIER);
			if(currentToken.kind == Token.PERIOD || currentToken.kind == Token.LBRACKET)
			while(currentToken.kind == Token.PERIOD || currentToken.kind == Token.LBRACKET)
				parseSelector();
		}
		else if(currentToken.kind == Token.LPAREN)
		{
			expect(Token.LPAREN);
			parseExpression();
			expect(Token.RPAREN);
		}
		else if(currentToken.kind == Token.NOT)
		{//Bdii: Forgot {...}
			expect(Token.NOT);
			parseFactor();
		}
		else	//Bdii: Forgot else
		new Error("Syntax error: Integer or Identifier or Period or ( or NOT is expected.", currentToken.line);
	}
	// DONE


// ************* NEXT THREE METHODS BELOW **************

  
  // VariableAccess = VariableName VariableAccessTail.
  // VariableAccess = VariableName {Selector}.
  void parseVariableAccess() {
		expect(Token.IDENTIFIER);
			while(currentToken.kind == Token.PERIOD || currentToken.kind == Token.LBRACKET)
				parseSelector();
		
	}
  //RelationalOprator = "<" | "=" | ">" | "<>".
  private void parseRelationalOperator()
  {
	  if(currentToken.kind == Token.LESS)
		  expect(Token.LESS);
	  else if (currentToken.kind == Token.EQUAL)
		  expect(Token.EQUAL);
	  else if (currentToken.kind == Token.GREATER)
		  expect(Token.GREATER);
	  else if (currentToken.kind == Token.NOTEQUAL)
		  expect(Token.NOTEQUAL);
	  else new Error("Syntax error: < or = or > or <> is expected",currentToken.line);
	  
  }  
	// DONE

}
