//David Jimenez, Ethan McMurray, and Mason Richardson
//CSC 415 Programming Languages
//Assignment 5

public class Parser 
{
	public Token next;
	public Lexer lexer;
	
	public Parser(String file)
	{
		lexer = new Lexer(file);
		next = lexer.lex();
	}
	
	private void match(String expectedToken)
	{
		if(expectedToken.equals("EOF")) 
		{
			System.out.println("Reached End Of File.");
			System.exit(0);
		}
		else if (!expectedToken.equals(next.getLexeme()))
		{
			System.out.println("Syntax Error: Unexpected token \"" + expectedToken + "\"");
			System.exit(0);
		}
      
        next = lexer.lex();
	}
	
	private void stmt_list()
	{
        /* This program is mostly recursive, and starts with stmt_list.
        It then goes to stmt, and follows the Kickflip grammar from there. */
      
		System.out.println("Begin <stmt_list>");
		
		stmt();
		try {
			while(next.getDescription().equals("PRINT keyword") ||  next.getDescription().equals("LET keyword") || next.getDescription().equals("LOOP keyword") || next.getDescription().equals("IF keyword"))
			{
				stmt();
			}
		}
		catch (NullPointerException e)
		{
			//If next Lexeme is not available, the statements are done and this catches the possible exception.
		}
	}

	private void stmt()
	{
		System.out.println("Begin <stmt>");
		
		if (next.getDescription().equals("PRINT keyword"))
          	print_stmt();
		else if (next.getDescription().equals("LET keyword"))
			let_stmt();
		else if (next.getDescription().equals("LOOP keyword"))
			loop_stmt();
		else if (next.getDescription().equals("IF keyword"))
			if_stmt();
		else
		{
			System.out.println("Syntax Error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void print_stmt()
	{
		System.out.println("Begin <print_stmt>");
		
		match("PRINT");
     	System.out.println("Matched PRINT");
		expr();
		match(";");
      	System.out.println("Matched ;");
	}
	
	private void let_stmt()
	{
		System.out.println("Begin <let_stmt>");
		
		match("LET");
  		System.out.println("Matched LET");
		id();
		match("=");
      	System.out.println("Matched =");
		expr();
		match(";");
      	System.out.println("Matched ;");
	}
	
	private void loop_stmt()
	{
		System.out.println("Begin <loop_stmt>");
		
		match("LOOP");
  		System.out.println("Matched LOOP");
		match("(");
  		System.out.println("Matched (");
		bool_expr();
		match(")");
  		System.out.println("Matched )");
		stmt_list();
		match("ENDLOOP");
  		System.out.println("Matched ENDLOOP");
	}
	
	private void if_stmt()
	{
		System.out.println("Begin <if_stmt>");
		
		match("IF");
		System.out.println("Matched IF");
		match("(");
		System.out.println("Matched (");
		bool_expr();
		match(")");
		System.out.println("Matched )");
		stmt_list();
		
		while (next.getLexeme().equals("OTHIF"))
		{
			match("OTHIF");
			System.out.println("Matched OTHIF");
			match("(");
			System.out.println("Matched (");
			bool_expr();
			match(")");
			System.out.println("Matched )");
			stmt_list();
		}
		
		if (next.getLexeme().equals("OTHERWISE"))
		{
			match("OTHERWISE");
          	System.out.println("Matched OTHERWISE");
			stmt_list();
		}
		
		match("ENDIF");
     	System.out.println("Matched ENDIF");
      
	}
	
	private void expr()
	{
		System.out.println("Begin <expr>");
		
		if (next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
		{
			term();
			
			if (next.getDescription().equals("add/concatenate") || next.getDescription().equals("subtract"))
			{
				if (next.getDescription().equals("add/concatenate"))
				{
					match("+");
                  	System.out.println("Matched +");
					term();
				}
				
				else if(next.getDescription().equals("subtract"))
				{
					match("-");
                  	System.out.println("Matched -");
					term();
				}
			}
          
          	else if (next.getDescription().equals("semicolon"))
            {	

            }
			
			else
			{
				System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
				System.exit(0);
			}
		}
		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void term()
	{
		System.out.println("Begin <term>");
		
		if (next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
		{
			factor();
			
			if (next.getDescription().equals("multiply") || next.getDescription().equals("divide") || next.getDescription().equals("modulus"))
			{
				if (next.getDescription().equals("multiply"))
				{
					match("*");
                  	System.out.println("Matched *");
					factor();
				}
				
				else if (next.getDescription().equals("divide"))
				{
					match("/");
                  	System.out.println("Matched /");
					factor();
				}
				
				else if (next.getDescription().equals("modulus"))
				{
					match("R");
                  	System.out.println("Matched R");
					factor();
				}
			}
          
          	else if (next.getDescription().equals("semicolon") || next.getDescription().equals("add/concatenate") || next.getDescription().equals("subtract"))
            {

            }
			
			else
			{
				System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
				System.exit(0);
			}
		}
		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void factor()
	{
		System.out.println("Begin <factor>");
		
		if (next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
		{
			value();			
		}
		
		else if (next.getDescription().equals("open parenthesis"))
		{
			match("(");
          	System.out.println("Matched (");
			if (next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
			{
				expr();
			}
			
			else
			{
				System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
				System.exit(0);
			}
			match(")");
         	System.out.println("Matched )");
		}
		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void bool_expr() 
	{
		System.out.println("Begin <bool_expr>");
		
		if(next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
        {
            bool_term();
            
            if(next.getDescription().equals("logical or"))
            {
                match("|");
              	System.out.println("Matched |");
                bool_expr();
            }
        }
		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void bool_term() 
	{
		System.out.println("Begin <bool_term>");
		
		if(next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
        {
            bool_factor();
            if(next.getDescription().equals("logical and")) {
                match("&");
              	System.out.println("Matched &");
                bool_factor();
            }
        }
      		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void bool_factor()
	{
		System.out.println("Begin <bool_factor>");
		
		if(next.getDescription().equals("identifier") || next.getDescription().equals("numeric literal") || next.getDescription().equals("string literal"))
        {
            if(next.getDescription().equals("logical not"))
            {
                match("~");
              	System.out.println("Matched ~");
                match("(");
              	System.out.println("Matched (");
                bool_expr();
                match(")");
              	System.out.println("Matched )");
            }
            
            else {
                value();
                
                if(next.getDescription().equals("is equal to"))
                {
                    match("==");
                  	System.out.println("Matched ==");
                    value();
                }
                
                else if(next.getDescription().equals("less than"))
                {
                    match("<");
                 	System.out.println("Matched <");
                    value();
                }
                
                else if(next.getDescription().equals("greater than"))
                {
                    match(">");
                  	System.out.println("Matched >");
                    value();
                }
            }
            
        }
		
		else
		{
			System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
			System.exit(0);
		}
	}
	
	private void value()
	{
		System.out.println("Begin <value>");
      
        /* We do not check for Syntax Errors here, since it has already been checked
        for in factor() and bool_factor() */
      
      	if(next.getDescription().equals("identifier"))
        {
            id();
        }
        
        else
        {
            literal();
        }
	}
	
	private void id()
    {
        /* We check for errors here, due to <id> being explicitly
        stated in the let statement. */
      
        System.out.println("Begin <id>");
      
        if(next.getDescription().equals("identifier"))
        {
            System.out.println("Matched " + next.getLexeme());
          	next = lexer.lex();
        }

        else
        {
            System.out.println("Syntax error: Unexpected token \"" + next.getLexeme() + "\"");
            System.exit(0);
        }
    }

    private void literal()
    {
        /* We do not check for errors here, since <literal> is only
        ever passed through <value> and never explicitly stated. */
      
        System.out.println("Begin <literal>");
        System.out.println("Matched " + next.getLexeme());
      	next = lexer.lex();
    }
	
	public static void main(String[] args) 
	{
		Parser parser = new Parser(args[0]);
		parser.stmt_list();
		parser.match("EOF");
	}
}
