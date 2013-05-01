package php.java.bridge;
public class PhpDebuggerPHP {
    private static final String data = "<?php /*-*- mode: php; tab-width:4 -*-*/\n"+
"\n"+
"  /**\n"+
"   *  PHPDebugger.php -- The PHP debugger (JavaScript GUI)\n"+
"   * \n"+
"   * Copyright (C) 2009 Jost Boekemeier\n"+
"   * \n"+
"   * The PHPDebugger (\"the library\") is free software; you can\n"+
"   * redistribute it and/or modify it under the terms of the GNU General\n"+
"   * Public License as published by the Free Software Foundation; either\n"+
"   * version 2, or (at your option) any later version.\n"+
"   * \n"+
"   * The library is distributed in the hope that it will be useful, but\n"+
"   * WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
"   * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n"+
"   * General Public License for more details.\n"+
"   * \n"+
"   * You should have received a copy of the GNU General Public License\n"+
"   * along with the PHPDebugger; see the file COPYING. If not, write to the\n"+
"   * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA\n"+
"   * 02111-1307 USA.\n"+
"   * \n"+
"   * Linking this file statically or dynamically with other modules is\n"+
"   * making a combined work based on this library. Thus, the terms and\n"+
"   * conditions of the GNU General Public License cover the whole\n"+
"   * combination.\n"+
"   * \n"+
"   * As a special exception, the copyright holders of this library give you\n"+
"   * permission to link this library with independent modules to produce an\n"+
"   * executable, regardless of the license terms of these independent\n"+
"   * modules, and to copy and distribute the resulting executable under\n"+
"   * terms of your choice, provided that you also meet, for each linked\n"+
"   * independent module, the terms and conditions of the license of that\n"+
"   * module. An independent module is a module which is not derived from\n"+
"   * or based on this library. If you modify this library, you may extend\n"+
"   * this exception to your version of the library, but you are not\n"+
"   * obligated to do so. If you do not wish to do so, delete this\n"+
"   * exception statement from your version. \n"+
"   *\n"+
"   * Usage:\n"+
"   * \n"+
"   * - include() this file at the beginning of your script\n"+
"   *\n"+
"   * - browse to your script using firefox \n"+
"   *\n"+
"   * - set breakpoints using the JavaScript GUI, click on any variable or included file to visit that variable or file\n"+
"   * \n"+
"   * - click on the stop button to terminate the debug session\n"+
"   *\n"+
"   * @category   java\n"+
"   * @package    pdb\n"+
"   * @author     Jost Boekemeier\n"+
"   * @license    GPL\n"+
"   * @version    1.0\n"+
"   * @link       http://php-java-bridge.sf.net/phpdebugger\n"+
"   * @see        PHPDebugger.inc\n"+
"   */\n"+
"\n"+
"/** @access private */\n"+
"define (\"PDB_DEBUG\", 0);\n"+
"set_time_limit (0);\n"+
"\n"+
"$pdb_version = phpversion();\n"+
"if ((version_compare(\"5.3.0\", $pdb_version, \">\")))\n"+
"  trigger_error(\"<br><strong>PHP ${pdb_version} too old.</strong><br>\\nPlease set the path to a PHP >= 5.3 executable, see php_exec in the WEB-INF/web.xml\", E_USER_ERROR);\n"+
"\n"+
" \n"+
"/**\n"+
" * a simple logger\n"+
" * @access private\n"+
" */\n"+
"class pdb_Logger {\n"+
"  const FATAL = 1;\n"+
"  const INFO = 2;\n"+
"  const VERBOSE = 3;\n"+
"  const DEBUG = 4;\n"+
"\n"+
"  private static $logLevel = 0;\n"+
"  private static $logFileName;\n"+
"  private static function println($msg, $level) {\n"+
"	if (!self::$logLevel) self::$logLevel=PDB_DEBUG?self::DEBUG:self::INFO;\n"+
"	if ($level <= self::$logLevel) {\n"+
"	  static $file = null;\n"+
"	  if(!isset(self::$logFileName)) {\n"+
"		self::$logFileName = $_SERVER['DOCUMENT_ROOT'].DIRECTORY_SEPARATOR.\"pdb_PHPDebugger.log\";\n"+
"	  }\n"+
"	  if (!$file) $file = fopen(self::$logFileName, \"ab\") or die(\"fopen\");\n"+
"	  fwrite($file, time().\": \");\n"+
"	  fwrite($file, $msg.\"\\n\");\n"+
"	  fflush($file);\n"+
"	}\n"+
"  }\n"+
"\n"+
"  public static function logFatal($msg) {\n"+
"	self::println($msg, self::FATAL);\n"+
"  }\n"+
"  public static function logInfo($msg) {\n"+
"	self::println($msg, self::INFO);\n"+
"  }\n"+
"  public static function logMessage($msg) {\n"+
"	self::println($msg, self::VERBOSE);\n"+
"  }\n"+
"  public static function logDebug($msg) {\n"+
"	self::println($msg, self::DEBUG);\n"+
"  }\n"+
"  public static function debug($msg) {\n"+
"	self::logDebug($msg);\n"+
"  }\n"+
"  public static function log($msg) {\n"+
"	self::logMessage($msg);\n"+
"  }\n"+
"  public static function setLogLevel($level) {\n"+
"	self::$logLevel=$level;\n"+
"  }\n"+
"  public static function setLogFileName($name) {\n"+
"	self::$logFileName = $name;\n"+
"  }\n"+
"}\n"+
"\n"+
"/**\n"+
" * @access private\n"+
" */\n"+
"interface pdb_Queue {\n"+
"\n"+
"  /** \n"+
"   * Read a string from the queue\n"+
"   * @return string a json encoded string of values\n"+
"   * @access private\n"+
"   */\n"+
"  public function read();\n"+
"\n"+
"  /**\n"+
"   * Write a string to the queue\n"+
"   * @param string a json encoded string of values or TRUE\n"+
"   * @access private\n"+
"   */\n"+
"  public function write($val);\n"+
"\n"+
"  /**\n"+
"   * Set the script output before server shutdown\n"+
"   * @access private\n"+
"   */\n"+
"  public function setOutput($output);\n"+
"\n"+
"  /**\n"+
"   * Get the script output\n"+
"   * @access private\n"+
"   */\n"+
"  public function getOutput();\n"+
"\n"+
"  /**\n"+
"   * Mark the channel as dead. If marked, read will return boolean\n"+
"   * TRUE, write will do nothing.\n"+
"   * @access private\n"+
"   */\n"+
"  public function shutdown();\n"+
"}\n"+
"\n"+
"/**\n"+
" * This class represents the debugger back end connection. It\n"+
" * communicates with the debugger front end using a shared-memory queue.\n"+
" * It is slow, but it does not require any special library.\n"+
" * @access private\n"+
" */\n"+
"class pdb_PollingServerConnection implements pdb_Queue {\n"+
"  protected $id;\n"+
"  protected $role, $to;\n"+
"  protected $chanTrm; // \"back end terminated\" flag\n"+
"  protected $output;\n"+
"  const TIMER_DURATION = 200000; // every 200ms\n"+
"\n"+
"  /**\n"+
"   * Create a new communication using a unique id\n"+
"   * @access private\n"+
"   */\n"+
"  public function pdb_PollingServerConnection($id) {\n"+
"	$this->id = $id;\n"+
"	$this->chanTrm = \"pdb_trmserver{$this->id}\";\n"+
"	$this->output = \"<missing>\";\n"+
"\n"+
"	$this->prepareCookies();\n"+
"	$this->init();\n"+
"  }\n"+
"\n"+
"  protected function checkTrm() {\n"+
"	return false!==$_SESSION[$this->chanTrm];\n"+
"  }\n"+
"\n"+
"  protected function prepareCookies() {\n"+
"	ini_set(\"session.use_cookies\", true);\n"+
"	session_start();\n"+
"	session_write_close();\n"+
"\n"+
"	/* avoid PHP bug, which repeats set-cookie header for each\n"+
"	   iteration of session_start/session_write_close */\n"+
"	ini_set(\"session.use_cookies\", false);\n"+
"  }\n"+
"\n"+
"  protected function init() {\n"+
"	session_start();\n"+
"\n"+
"	$this->role = \"client\";\n"+
"	$this->to  = \"server\";\n"+
"\n"+
"	$chanCtr = \"pdb_ctr{$this->role}{$this->id}\";\n"+
"	$chan = \"pdb_{$this->role}{$this->id}\";\n"+
"	unset ($_SESSION[$chan]);\n"+
"	unset ($_SESSION[$chanCtr]);\n"+
"\n"+
"	$this->role = \"server\";\n"+
"	$this->to  = \"client\";\n"+
"\n"+
"	$chanCtr = \"pdb_ctr{$this->role}{$this->id}\";\n"+
"	$chan = \"pdb_{$this->role}{$this->id}\";\n"+
"	unset ($_SESSION[$chan]);\n"+
"	unset ($_SESSION[$chanCtr]);\n"+
"\n"+
"	if (isset($_SESSION[$this->chanTrm]) && !$this->checkTrm()) {\n"+
"	  $_SESSION[$this->chanTrm] = true;\n"+
"	  session_write_close();\n"+
"	  sleep(1);\n"+
"	  session_start();\n"+
"	}\n"+
"	$_SESSION[$this->chanTrm] = false;\n"+
"	session_write_close();\n"+
"  }\n"+
"\n"+
"  protected function poll() {\n"+
"	$val = \"\";\n"+
"	$chanCtr = \"pdb_ctr{$this->role}{$this->id}\";\n"+
"	$chan = \"pdb_{$this->role}{$this->id}\";\n"+
"	session_start();\n"+
"	if (!($val = $this->checkTrm())) {\n"+
"	  if(!isset($_SESSION[$chanCtr])) { \n"+
"		$_SESSION[$chan] = array(); \n"+
"		$_SESSION[$chanCtr]=0; \n"+
"	  }\n"+
"	  $seq = $_SESSION[$chanCtr];\n"+
"	  $seqNext = count($_SESSION[$chan]);\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"...{$this->role}, {$this->id} poll next # ${seqNext} (${seq}) ...\");\n"+
"	  if ($seqNext > $seq) {\n"+
"		$val = json_decode($_SESSION[$chan][$seq]);\n"+
"		$_SESSION[$chan][$seq]=null;\n"+
"		$_SESSION[$chanCtr]++;\n"+
"		if (PDB_DEBUG) pdb_Logger::debug(\"...{$this->role}, {$this->id} polled next # ${seqNext} (${seq}), got: {$val->seq}\");\n"+
"	  }\n"+
"	}\n"+
"	session_write_close();\n"+
"	return $val;\n"+
"  }\n"+
"  protected function send($val) {\n"+
"	\n"+
"	$seq = $val->seq;\n"+
"	$chan = \"pdb_{$this->to}{$this->id}\";\n"+
"	session_start();\n"+
"	if (!$this->checkTrm()) $_SESSION[$chan][$seq]=json_encode($val);\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"...{$this->role}, {$this->id} send: ${seq} ...\");\n"+
"	session_write_close();\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * read a new value from the read queue\n"+
"   * @access private\n"+
"   */\n"+
"  public function read() {\n"+
"	$val = null;\n"+
"	$cntr = 0;\n"+
"	while(!($val=$this->poll())) {\n"+
"	  if ($cntr<=20) {\n"+
"		$cntr++;\n"+
"		usleep(self::TIMER_DURATION);\n"+
"	  } else {\n"+
"		usleep(self::TIMER_DURATION*5);\n"+
"	  }\n"+
"	}\n"+
"	return $val === true ? null : $val;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * write a new value to the write queue\n"+
"   * @access private\n"+
"   */\n"+
"  public function write($val) {\n"+
"	$this->send((object)$val);\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * Set the script output\n"+
"   * @access private\n"+
"   */\n"+
"  public function setOutput($output) {\n"+
"	$this->output = $output;\n"+
"  }\n"+
"  /**\n"+
"   * Get the script output\n"+
"   * @access private\n"+
"   */\n"+
"  public function getOutput() {\n"+
"	return $_SESSION[$this->chanTrm];\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * shut down the communication channel\n"+
"   */\n"+
"  public function shutdown() {\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"session terminated: {$this->chanTrm}\");\n"+
"	session_start();\n"+
"	$_SESSION[$this->chanTrm] = $this->output;\n"+
"	session_write_close();\n"+
"  }\n"+
"}  \n"+
"\n"+
"/**\n"+
" * This class represents the debugger front end connection. It\n"+
" * communicates with the debugger back end using a shared-memory queue.\n"+
" * It is slow, but it does not require any special library.\n"+
" * @access private\n"+
" */\n"+
"class pdb_PollingClientConnection extends pdb_PollingServerConnection {\n"+
"  private $seq;\n"+
"\n"+
"  protected function init() {\n"+
"	$this->role = \"client\";\n"+
"	$this->to  = \"server\";\n"+
"  }\n"+
"\n"+
"  protected function poll() {\n"+
"	$chan = \"pdb_{$this->role}{$this->id}\";\n"+
"	session_start();\n"+
"	if (!($val = $this->checkTrm())) {\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"...{$this->role}, {$this->id} poll for {$this->seq} ...\");\n"+
"	  if (isset($_SESSION[$chan][$this->seq])) {\n"+
"		$val = json_decode($_SESSION[$chan][$this->seq]);\n"+
"		if (PDB_DEBUG) pdb_Logger::debug(\"...{$this->role}, {$this->id} polled for {$this->seq}, got: {$val->seq}\");\n"+
"		unset($_SESSION[$chan][$this->seq]);\n"+
"	  }\n"+
"	}\n"+
"	session_write_close();\n"+
"	return $val;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * write a new value to the write queue\n"+
"   * @access private\n"+
"   */\n"+
"  public function write($val) {\n"+
"	$this->seq = $val->seq;\n"+
"\n"+
"	parent::write($val);\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * shut down the communication channel\n"+
"   * @access private\n"+
"   */\n"+
"  public function shutdown() {}\n"+
"}\n"+
"\n"+
"if (!class_exists(\"pdb_Parser\")) {\n"+
"  /**\n"+
"   * The PHP parser\n"+
"   * @access private\n"+
"   */\n"+
"  class pdb_Parser {\n"+
"	const BLOCK = 1;\n"+
"	const STATEMENT = 2;\n"+
"	const EXPRESSION = 3;\n"+
"	const FUNCTION_BLOCK = 4; // BLOCK w/ STEP() as last statement\n"+
"\n"+
"	private $scriptName, $content;\n"+
"	private $code;\n"+
"	private $output;\n"+
"	private $line, $currentLine;\n"+
"	private $beginStatement, $inPhp, $inDQuote;\n"+
" \n"+
"	/**\n"+
"	 * Create a new PHP parser\n"+
"	 * @param string the script name\n"+
"	 * @param string the script content\n"+
"	 * @access private\n"+
"	 */\n"+
"	public function pdb_Parser($scriptName, $content) {\n"+
"	  $this->scriptName = $scriptName;\n"+
"	  $this->content = $content;\n"+
"	  $this->code = token_get_all($content);\n"+
"	  $this->output = \"\";\n"+
"	  $this->line = $this->currentLine = 0;\n"+
"	  $this->beginStatement = $this->inPhp = $this->inDQuote = false;\n"+
"	}\n"+
"\n"+
"	private function toggleDQuote($chr) {\n"+
"	  if ($chr == '\"') $this->inDQuote = !$this->inDQuote;\n"+
"	}\n"+
"\n"+
"	private function each() {\n"+
"	  $next = each ($this->code);\n"+
"	  if ($next) {\n"+
"		$cur = current($this->code);\n"+
"		if (is_array($cur)) {\n"+
"		  $this->currentLine = $cur[2] + ($cur[1][0] == \"\\n\" ? substr_count($cur[1], \"\\n\") : 0);\n"+
"		  if ($this->isWhitespace($cur)) {\n"+
"			$this->write($cur[1]);\n"+
"			return $this->each();\n"+
"		  }\n"+
"		}\n"+
"		else \n"+
"		  $this->toggleDQuote($cur);\n"+
"	  }\n"+
"	  return $next;\n"+
"	}\n"+
"\n"+
"	private function write($code) {\n"+
"	  //echo \"write:::\".$code.\"\\n\";\n"+
"	  $this->output.=$code;\n"+
"	}\n"+
"\n"+
"	private function writeInclude($once) {\n"+
"	  $name = \"\";\n"+
"	  while(1) {\n"+
"		if (!$this->each()) die(\"parse error\");\n"+
"		$val = current($this->code);\n"+
"		if (is_array($val)) {\n"+
"		  $name.=$val[1];\n"+
"		} else {\n"+
"		  if ($val==';') break;\n"+
"		  $name.=$val;\n"+
"		}\n"+
"	  }\n"+
"	  if (PDB_DEBUG == 2) \n"+
"		$this->write(\"EVAL($name);\");\n"+
"	  else\n"+
"		$this->write(\"eval('?>'.pdb_startInclude($name, $once)); pdb_endInclude();\");\n"+
"	}\n"+
"\n"+
"	private function writeCall() {\n"+
"	  while(1) {\n"+
"		if (!$this->each()) die(\"parse error\");\n"+
"		$val = current($this->code);\n"+
"		if (is_array($val)) {\n"+
"		  $this->write($val[1]);\n"+
"		} else {\n"+
"		  $this->write($val);\n"+
"		  if ($val=='{') break;\n"+
"		}\n"+
"	  }\n"+
"	  $scriptName = addslashes($this->scriptName);\n"+
"	  $this->write(\"\\$__pdb_CurrentFrame=pdb_startCall(\\\"$scriptName\\\", {$this->currentLine});\");\n"+
"	}\n"+
"\n"+
"	private function writeStep($pLevel) {\n"+
"	  $token = current($this->code);\n"+
"	  if ($this->inPhp && !$pLevel && !$this->inDQuote && $this->beginStatement && !$this->isWhitespace($token) && ($this->line != $this->currentLine)) {\n"+
"		$line = $this->line = $this->currentLine;\n"+
"		$scriptName = addslashes($this->scriptName);\n"+
"		if (PDB_DEBUG == 2)\n"+
"		  $this->write(\";STEP($line);\");\n"+
"		else\n"+
"		  $this->write(\";pdb_step(\\\"$scriptName\\\", $line, pdb_getDefinedVars(get_defined_vars(), (isset(\\$this) ? \\$this : NULL)));\");\n"+
"	  }\n"+
"	}\n"+
"\n"+
"	private function writeNext() {\n"+
"	  $this->next();\n"+
"	  $token = current($this->code);\n"+
"	  if (is_array($token)) $token = $token[1];\n"+
"	  $this->write($token);\n"+
"	}\n"+
"\n"+
"	private function nextIs($chr) {\n"+
"	  $i = 0;\n"+
"	  while(each($this->code)) {\n"+
"		$cur = current($this->code);\n"+
"		$i++;\n"+
"		if (is_array($cur)) {\n"+
"		  switch ($cur[0]) {\n"+
"		  case T_COMMENT:\n"+
"		  case T_DOC_COMMENT:\n"+
"		  case T_WHITESPACE:\n"+
"			break;	/* skip */\n"+
"		  default: \n"+
"			while($i--) prev($this->code);\n"+
"			return false;	/* not found */\n"+
"		  }\n"+
"		} else {\n"+
"		  while($i--) prev($this->code);\n"+
"		  return $cur == $chr;	/* found */\n"+
"		}\n"+
"	  }\n"+
"	  while($i--) prev($this->code);\n"+
"	  return false;	/* not found */\n"+
"	}\n"+
"\n"+
"	private function nextTokenIs($ar) {\n"+
"	  $i = 0;\n"+
"	  while(each($this->code)) {\n"+
"		$cur = current($this->code);\n"+
"		$i++;\n"+
"		if (is_array($cur)) {\n"+
"		  switch ($cur[0]) {\n"+
"		  case T_COMMENT:\n"+
"		  case T_DOC_COMMENT:\n"+
"		  case T_WHITESPACE:\n"+
"			break;	/* skip */\n"+
"		  default: \n"+
"			while($i--) prev($this->code);\n"+
"			return (in_array($cur[0], $ar));\n"+
"		  }\n"+
"		} else {\n"+
"		  break; /* not found */\n"+
"		}\n"+
"	  }\n"+
"	  while($i--) prev($this->code);\n"+
"	  return false;	/* not found */\n"+
"	}\n"+
"\n"+
"	private function isWhitespace($token) {\n"+
"	  $isWhitespace = false;\n"+
"	  switch($token[0]) {\n"+
"	  case T_COMMENT:\n"+
"	  case T_DOC_COMMENT:\n"+
"	  case T_WHITESPACE:\n"+
"		$isWhitespace = true;\n"+
"		break;\n"+
"	  }\n"+
"	  return $isWhitespace;\n"+
"	}\n"+
"	private function next() {\n"+
"	  if (!$this->each()) trigger_error(\"parse error\", E_USER_ERROR);\n"+
"	}\n"+
"\n"+
"	private function parseBlock () {\n"+
"	  $this->parse(self::BLOCK);\n"+
"	}\n"+
"	private function parseFunction () {\n"+
"	  $this->parse(self::FUNCTION_BLOCK);\n"+
"	}\n"+
"	private function parseStatement () {\n"+
"	  $this->parse(self::STATEMENT);\n"+
"	}\n"+
"	private function parseExpression () {\n"+
"	  $this->parse(self::EXPRESSION);\n"+
"	}\n"+
"\n"+
"	private function parse ($type) {\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"parse:::$type\");\n"+
"\n"+
"	  $this->beginStatement = true;\n"+
"	  $pLevel = 0;\n"+
"\n"+
"	  do {\n"+
"		$token = current($this->code);\n"+
"		if (!is_array($token)) {\n"+
"		  if (PDB_DEBUG) pdb_Logger::debug(\":::\".$token);\n"+
"		  if (!$pLevel && $type==self::FUNCTION_BLOCK && $token=='}') $this->writeStep($pLevel);\n"+
"		  $this->write($token);\n"+
"		  if ($this->inPhp && !$this->inDQuote) {\n"+
"			$this->beginStatement = false; \n"+
"			switch($token) {\n"+
"			case '(': \n"+
"			  $pLevel++;\n"+
"			  break;\n"+
"			case ')':\n"+
"			  if (!--$pLevel && $type==self::EXPRESSION) return;\n"+
"			  break;\n"+
"			case '{': \n"+
"			  $this->next();\n"+
"			  $this->parseBlock(); \n"+
"			  break;\n"+
"			case '}': \n"+
"			  if (!$pLevel) return;\n"+
"			  break;\n"+
"			case ';':\n"+
"			  if (!$pLevel) {\n"+
"				if ($type==self::STATEMENT) return;\n"+
"				$this->beginStatement = true; \n"+
"			  }\n"+
"			  break;\n"+
"			}\n"+
"		  }\n"+
"		} else {\n"+
"		  if (PDB_DEBUG) pdb_Logger::debug(\":::\".$token[1].\":(\".token_name($token[0]).')');\n"+
"\n"+
"		  if ($this->inDQuote) {\n"+
"			$this->write($token[1]);\n"+
"			continue;\n"+
"		  }\n"+
"\n"+
"		  switch($token[0]) {\n"+
"\n"+
"		  case T_OPEN_TAG: \n"+
"		  case T_START_HEREDOC:\n"+
"		  case T_OPEN_TAG_WITH_ECHO: \n"+
"			$this->beginStatement = $this->inPhp = true;\n"+
"			$this->write($token[1]);\n"+
"			break;\n"+
"\n"+
"		  case T_END_HEREDOC:\n"+
"		  case T_CLOSE_TAG: \n"+
"			$this->writeStep($pLevel);\n"+
"\n"+
"			$this->write($token[1]);\n"+
"			$this->beginStatement = $this->inPhp = false; \n"+
"			break;\n"+
"\n"+
"		  case T_FUNCTION:\n"+
"			$this->write($token[1]);\n"+
"			$this->writeCall();\n"+
"			$this->next();\n"+
"			$this->parseFunction();\n"+
"			$this->beginStatement = true;\n"+
"			break;\n"+
"\n"+
"		  case T_ELSE:\n"+
"			$this->write($token[1]);\n"+
"			if ($this->nextIs('{')) {\n"+
"			  $this->writeNext();\n"+
"			  $this->next();\n"+
"\n"+
"			  $this->parseBlock();\n"+
"			} else {\n"+
"			  $this->next();\n"+
"\n"+
"			  /* create an artificial block */\n"+
"			  $this->write('{');\n"+
"			  $this->beginStatement = true;\n"+
"			  $this->writeStep($pLevel);\n"+
"			  $this->parseStatement();\n"+
"			  $this->write('}');\n"+
"\n"+
"			}\n"+
"			if ($type==self::STATEMENT) return;\n"+
"\n"+
"			$this->beginStatement = true;\n"+
"			break;\n"+
"\n"+
"		  case T_DO:\n"+
"			$this->writeStep($pLevel);\n"+
"			$this->write($token[1]);\n"+
"			if ($this->nextIs('{')) {\n"+
"			  $this->writeNext();\n"+
"			  $this->next();\n"+
"\n"+
"			  $this->parseBlock();\n"+
"			  $this->next();\n"+
"\n"+
"			} else {\n"+
"			  $this->next();\n"+
"\n"+
"			  /* create an artificial block */\n"+
"			  $this->write('{');\n"+
"			  $this->beginStatement = true;\n"+
"			  $this->writeStep($pLevel);\n"+
"			  $this->parseStatement();\n"+
"			  $this->next();\n"+
"			  $this->write('}');\n"+
"			}\n"+
"			$token = current($this->code);\n"+
"			$this->write($token[1]);\n"+
"\n"+
"			if ($token[0]!=T_WHILE) trigger_error(\"parse error\", E_USER_ERROR);\n"+
"			$this->next();\n"+
"			$this->parseExpression();\n"+
"\n"+
"			if ($type==self::STATEMENT) return;\n"+
"\n"+
"			$this->beginStatement = true;\n"+
"			break;\n"+
"\n"+
"		  case T_CATCH:\n"+
"		  case T_IF:\n"+
"		  case T_ELSEIF:\n"+
"		  case T_FOR:\n"+
"		  case T_FOREACH:\n"+
"		  case T_WHILE:\n"+
"			$this->writeStep($pLevel);\n"+
"\n"+
"			$this->write($token[1]);\n"+
"			$this->next();\n"+
"\n"+
"			$this->parseExpression();\n"+
"\n"+
"			if ($this->nextIs('{')) {\n"+
"			  $this->writeNext();\n"+
"			  $this->next();\n"+
"\n"+
"			  $this->parseBlock();\n"+
"\n"+
"\n"+
"			} else {\n"+
"			  $this->next();\n"+
"			  /* create an artificial block */\n"+
"			  $this->write('{');\n"+
"			  $this->beginStatement = true;\n"+
"			  $this->writeStep($pLevel);\n"+
"			  $this->parseStatement();\n"+
"			  $this->write('}');\n"+
"			}\n"+
"\n"+
"			if ($this->nextTokenIs(array(T_ELSE, T_ELSEIF, T_CATCH))) {\n"+
"			  $this->beginStatement = false;\n"+
"			} else {\n"+
"			  if ($type==self::STATEMENT) return;\n"+
"			  $this->beginStatement = true;\n"+
"			}\n"+
"			break;\n"+
"\n"+
"		  case T_REQUIRE_ONCE:\n"+
"		  case T_INCLUDE_ONCE: \n"+
"		  case T_INCLUDE: \n"+
"		  case T_REQUIRE: \n"+
"			$this->writeStep($pLevel);\n"+
"			$this->writeInclude((($token[0]==T_REQUIRE_ONCE) || ($token[0]==T_INCLUDE_ONCE)) ? 1 : 0);\n"+
"\n"+
"			if ($type==self::STATEMENT) return;\n"+
"\n"+
"			$this->beginStatement = true;\n"+
"			break;\n"+
"\n"+
"		  case T_CLASS:\n"+
"			$this->write($token[1]);\n"+
"			$this->writeNext();\n"+
"			if ($this->nextIs('{')) {\n"+
"			  $this->writeNext();\n"+
"			  $this->next();\n"+
"			  $this->parseBlock(); \n"+
"			  $this->beginStatement = true;\n"+
"			} else {\n"+
"			  $this->writeNext();\n"+
"			  $this->beginStatement = false;\n"+
"			}\n"+
"			break;\n"+
"\n"+
"		  case T_CASE:\n"+
"		  case T_DEFAULT:\n"+
"		  case T_PUBLIC:\n"+
"		  case T_PRIVATE:\n"+
"		  case T_PROTECTED:\n"+
"		  case T_STATIC:\n"+
"		  case T_CONST:\n"+
"		  case T_GLOBAL:\n"+
"		  case T_ABSTRACT:\n"+
"			$this->write($token[1]);\n"+
"			$this->beginStatement = false;\n"+
"			break;\n"+
"\n"+
"		  default:\n"+
"			$this->writeStep($pLevel);\n"+
"			$this->write($token[1]);\n"+
"			$this->beginStatement = false;\n"+
"			break;\n"+
"	\n"+
"		  }\n"+
"		}\n"+
"	  } while($this->each());\n"+
"	}\n"+
"\n"+
"	/**\n"+
"	 * parse the given PHP script\n"+
"	 * @return the parsed PHP script\n"+
"	 * @access private\n"+
"	 */\n"+
"	public function parseScript() {\n"+
"	  do {\n"+
"		$this->parseBlock();\n"+
"	  } while($this->each());\n"+
"\n"+
"	  return $this->output;\n"+
"	}\n"+
"  }\n"+
"}\n"+
"\n"+
"\n"+
"/**\n"+
" * This structure represents the debugger front-end. It is used by the\n"+
" * JavaScript code to communicate with the debugger back end.\n"+
" * @access private\n"+
" */\n"+
"class pdb_JSDebuggerClient {\n"+
"  private static function getDebuggerFilename() {\n"+
"	$script = __FILE__;\n"+
"	$scriptName = basename($script);\n"+
"	return realpath($scriptName == \"PHPDebugger.php\" ? $script :\"java/PHPDebugger.php\");\n"+
"  }\n"+
"	\n"+
"  private static function getCurrentRootDir() {\n"+
"	$scriptName = $_SERVER['SCRIPT_NAME'];\n"+
"	$scriptFilename = $_SERVER['SCRIPT_FILENAME'];\n"+
" \n"+
"	$scriptDirName = dirname($scriptName);\n"+
"	$scriptDir   = dirname($scriptFilename);\n"+
" \n"+
"	if ((strlen($scriptDirName)>1) && ($scriptDirName[1]=='~')) {\n"+
"	  $scriptDirName = ltrim($scriptDirName, \"/\");\n"+
"	  $idx = strpos($scriptDirName, '/');\n"+
"	  $scriptDirName = $idx===false ? '' : substr($scriptDirName, $idx);\n"+
"	} elseif ((strlen($scriptDirName)==1) && (($scriptDirName[0]=='/') || ($scriptDirName[0]=='\\\\'))) {\n"+
"	  $scriptDirName = '';\n"+
"	}\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"scriptDir: $scriptDir, scriptDirName: $scriptDirName\");\n"+
"\n"+
"	if ((strlen($scriptDir) < strlen($scriptDirName)) || ($scriptDirName &&\n"+
"														  (substr($scriptDir, -strlen($scriptDirName)) != $scriptDirName)))\n"+
"	  return null;\n"+
"	else\n"+
"	  return substr($scriptDir, 0, strlen($scriptDir)-strlen($scriptDirName));\n"+
"  }\n"+
"  /*\n"+
"   * Return the script name\n"+
"   * Example: %2Fopt%2Fappserv%2Fapache-tomcat-6.0.14%2Fwebapps%2FJavaBridge%2Ftest.php\"\n"+
"   *\n"+
"   * @return An urlencoded file name.\n"+
"   */\n"+
"  public static function getDebugScriptName() {\n"+
"	return urlencode($_SERVER['SCRIPT_FILENAME']);\n"+
"  }\n"+
"  /**\n"+
"   * Return the debugger URL. \n"+
"   * Example: \"/JavaBridge/java/PHPDebugger.php?source=settings.php\"\n"+
"   *\n"+
"   * @return The debugger URL.\n"+
"   * @access private\n"+
"   */\n"+
"  public static function getDebuggerURL() {\n"+
"	$path = self::getDebuggerFilename();\n"+
"	if (!$path) \n"+
"	  trigger_error(\"java/PHPDebugger.php not found in document root\", E_USER_ERROR);\n"+
"\n"+
"	$root = self::getCurrentRootDir();\n"+
"\n"+
"	$scriptName = $_SERVER['SCRIPT_NAME'];\n"+
"\n"+
"	$scriptDirName = dirname($scriptName);\n"+
"	$prefix = '';\n"+
"	if ((strlen($scriptDirName)>1) && ($scriptDirName[1]=='~')) {\n"+
"	  $scriptDirName = ltrim($scriptDirName, \"/\");\n"+
"	  $idx = strpos($scriptDirName, '/');\n"+
"	  $prefix = '/' . ($idx ? substr($scriptDirName, 0, $idx): $scriptDirName);\n"+
"	}\n"+
"  \n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"serverRoot: $root - path: $path\");\n"+
"	if ($root && (strlen($root) < strlen($path)) && (!strncmp($path, $root, strlen($root))))\n"+
"	  $path = \"${prefix}\" . str_replace('\\\\', '/', substr($path, strlen($root)));\n"+
"	else // could not calculate debugger path\n"+
"	  $path = dirname($_SERVER['SCRIPT_NAME']) . \"/java/PHPDebugger.php\";\n"+
"\n"+
"	$pathInfo = isset($_SERVER['PATH_INFO']) ? $_SERVER['PATH_INFO'] : \"\";\n"+
"	$query = isset($_SERVER['QUERY_STRING']) ? $_SERVER['QUERY_STRING'] : \"\";\n"+
"\n"+
"	$url = \"${path}${pathInfo}\";\n"+
"	if ($query) $url .= \"?${query}\";\n"+
"	return $url;\n"+
"  }\n"+
"\n"+
"  public static function getPostData() {\n"+
"	$str = '';\n"+
"	foreach ($_POST as $key => $value) {\n"+
"	  if ($str) $str .= '&';\n"+
"	  $str .= $key . '=' . urlencode($value);\n"+
"	}\n"+
"	return $str;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * Get the server's uniqe session ID\n"+
"   * @return a uniqe session ID\n"+
"   * @access private\n"+
"   */ \n"+
"  public static function getServerID() {\n"+
"	// TODO: allow more than one debug session\n"+
"	return 1;\n"+
"  }\n"+
" \n"+
"  private static function getConnection($id) {\n"+
"	return new pdb_PollingClientConnection($id);\n"+
"  }\n"+
"  private static function stripslashes($value) {\n"+
"	 $value = is_array($value) ?\n"+
"	   array_map(\"self::stripslashes\", $value) :\n"+
"	   stripslashes($value);\n"+
"	 \n"+
"	 return $value;\n"+
"   }\n"+
"\n"+
"  /**\n"+
"   * Pass the command and arguments to the debug back end and\n"+
"   * output the response to JavaScript.\n"+
"   *\n"+
"   * @arg array The command arguments\n"+
"   * @access private\n"+
"   */\n"+
"  public static function handleRequest($vars) {\n"+
"		if (get_magic_quotes_gpc()) $vars = self::stripslashes($vars);\n"+
"	$msg = (object)$vars;\n"+
"	\n"+
"	if ($msg->cmd == \"begin\") sleep(1); // wait for the server to settle\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"beginHandleRequest: \".$msg->cmd);\n"+
"	$conn = self::getConnection($msg->serverID);\n"+
"	$conn->write($msg);\n"+
"\n"+
"	if (!($response = $conn->read())) \n"+
"	  $output = json_encode(array(\"cmd\"=>\"term\", \"output\"=>$conn->getOutput()));\n"+
"	else\n"+
"	  $output = json_encode($response);\n"+
"	\n"+
"	echo \"($output)\";\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"endHandleRequest\");\n"+
"  }\n"+
"}\n"+
"\n"+
"/**\n"+
" * The current view frame. Contains the current script name. May be\n"+
" * selected by clicking on a include() hyperlink Allows users to set\n"+
" * breakpoints for this file.\n"+
" *\n"+
" * View will be discarded when go, step, end is invoked.\n"+
" * @access private\n"+
" */\n"+
"class pdb_View {\n"+
"  /** The current script name */\n"+
"  public $scriptName;\n"+
"  /** Back-link to the parent or null */\n"+
"  public $parent;\n"+
"\n"+
"  protected $bpCounter, $lineCounter, $code;\n"+
"  /**\n"+
"   * Create a new view frame\n"+
"   * @param object the parent frame\n"+
"   * @param string the script name\n"+
"   */\n"+
"  public function pdb_View($parent, $scriptName) {\n"+
"	$this->parent = $parent;\n"+
"	$this->scriptName = $scriptName;\n"+
"\n"+
"	$this->bpCounter = $this->lineCounter = 1;\n"+
"	$this->code = null;\n"+
"  }\n"+
"\n"+
"  private function lineCB ($val) {\n"+
"	return $val.(string)$this->lineCounter++;\n"+
"  }\n"+
"  private function breakpointCB ($val) {\n"+
"	return 	$val.'id=\"bp_'.(string)$this->bpCounter++.'\"';\n"+
"  }\n"+
"  function replaceCallback($code, $split, $cb) {\n"+
"	$ar = explode($split, $code);\n"+
"	$last = array_pop($ar);\n"+
"	$ar = array_map($cb, $ar);\n"+
"	array_push($ar, $last);\n"+
"	return implode($ar);\n"+
"  }\n"+
"\n"+
"\n"+
"  /**\n"+
"   * Return a HTML representation of the current script\n"+
"   * @return string the clickable HTML representation of the current script\n"+
"   */\n"+
"  public function getHtmlScriptSource() {\n"+
"	if (!$this->code) {\n"+
"	  $c=\n"+
"		'<span class=\"breakpoint\" id=\"bp_\" onmousedown=\"return toggleBreakpoint(this, event);\">'.\n"+
"		'<span class=\"currentlineIndicator normal\"></span>'.\n"+
"		'<span class=\"linenumber\">line#</span>'.\n"+
"		'<span class=\"breakpointIndicator normal\"></span>'.\n"+
"		'</span><br />';\n"+
"\n"+
"	  $code=show_source($this->scriptName, true);\n"+
"\n"+
"	  // br => span id=pb_ ...\n"+
"	  $code = str_replace('<br />', $c, $code);\n"+
"	  // handle incomplete last line, identical to preg: '|(?<!<br >)</span>\\n</span>\\n</code>$|'\n"+
"	  $code = ereg_replace('\"(([^>])|([^/]>)|([^ ]/>)|([^r] />)|([^b]r />)|([^<]br />))(</span>\\n</span>\\n</code>)\"', '\\1 $c \\8', $code);\n"+
"\n"+
"	  $code = $this->replaceCallback($code, 'id=\"bp_\"', array($this, \"breakpointCB\"));\n"+
"	  $code = $this->replaceCallback($code, 'line#', array($this, \"lineCB\"));\n"+
"\n"+
"	  $this->code = $code;\n"+
"	}\n"+
"\n"+
"	return $this->code;\n"+
"  }\n"+
"}\n"+
"/**\n"+
" * The current view. Used to show the contents of a variable\n"+
" * @access private\n"+
" */\n"+
"class pdb_VariableView extends pdb_View {\n"+
"  /**\n"+
"   * Create a new variable view\n"+
"   * @param object the parent frame\n"+
"   * @param string the variable name\n"+
"   * @param string the variable value\n"+
"   */\n"+
"  public function pdb_VariableView($parent, $name, $value) {\n"+
"	parent::pdb_View($parent, $name);\n"+
"	$this->value = $value;\n"+
"  }\n"+
"  /**\n"+
"   * {@inheritDoc}\n"+
"   */\n"+
"  public function getHtmlScriptSource() {\n"+
"	return (highlight_string(print_r($this->value, true), true));\n"+
"  }\n"+
"}\n"+
"/**\n"+
" * The current execution frame. Contains the current run-time script\n"+
" * name along with its state\n"+
" * @access private\n"+
" */\n"+
"class pdb_Environment extends pdb_View {\n"+
"  /** bool true if a dynamic breakpoint should be inserted at the next line, false otherwise */\n"+
"  public $stepNext;\n"+
"  /** The execution vars */\n"+
"  public $vars;\n"+
"  /** The current line */\n"+
"  public $line;\n"+
"\n"+
"  /**\n"+
"   * Create a new execution frame\n"+
"   * @param string the script name\n"+
"   * @param bool true if a dynamic breakpoint should be inserted at the next line, false otherwise\n"+
"   */\n"+
"  public function pdb_Environment($parent, $scriptName, $stepNext) {\n"+
"	parent::pdb_View($parent, $scriptName);\n"+
"	$this->stepNext = $stepNext;\n"+
"	$this->line = -1;\n"+
"  }\n"+
"  /**\n"+
"   * Update the execution frame with the current state\n"+
"   * @param string the current script name\n"+
"   * @param int the current execution line\n"+
"   * @param mixed the current variables\n"+
"   */\n"+
"  public function update ($line, &$vars) {\n"+
"	$this->line = $line;\n"+
"	$this->vars = $vars;\n"+
"  }\n"+
"\n"+
"  public function __toString() {\n"+
"	return \"pdb_Environment: {$this->scriptName}, {$this->line}\";\n"+
"  }\n"+
"}\n"+
"/**\n"+
" * Represents a breakpoint\n"+
" * @access private\n"+
" */\n"+
"class pdb_Breakpoint {\n"+
"  /** The script name */\n"+
"  public $scriptName;\n"+
"  /** The current line */\n"+
"  public $line;\n"+
"  /** The breakpointName as seen by JavaScript */\n"+
"  public $breakpoint;\n"+
"  /* The breakpoint type (not used yet) */\n"+
"  public $type;\n"+
"\n"+
"  /**\n"+
"   * Create a new breakpoint\n"+
"   * @param string the breakpoint name\n"+
"   * @param string the script name\n"+
"   * @param int the line\n"+
"   */\n"+
"  public function pdb_Breakpoint($breakpointName, $scriptName, $line) {\n"+
"	$this->breakpoint = $breakpointName;\n"+
"	$this->scriptName = $scriptName;\n"+
"	$this->line = $line;\n"+
"\n"+
"	$this->type = 1;\n"+
"  }\n"+
"  /**\n"+
"   * @return the string representation of the breakpoint\n"+
"   */\n"+
"  public function __toString() {\n"+
"	return \"{$this->line}@{$this->scriptName}, js name: ({$this->breakpoint}, type: {$this->type})\";\n"+
"  }\n"+
"}\n"+
"/**\n"+
" * The current debug session. Contains the current environment stack,\n"+
" * script output and all breakpoints set by the client. An optional\n"+
" * view is set by the switchView command.\n"+
" * @access private\n"+
" */\n"+
"final class pdb_Session {\n"+
"  /** The collection of breakpoints */\n"+
"  public $breakpoints;\n"+
"\n"+
"  /** List of all frames */\n"+
"  public $allFrames;\n"+
"  /** The current top level frame */\n"+
"  public $currentTopLevelFrame;\n"+
"  /** The current execution frame */\n"+
"  public $currentFrame;\n"+
"  /** The current view */\n"+
"  public $currentView;\n"+
"  /** The script output */\n"+
"  public $output;\n"+
"\n"+
"  /**\n"+
"   * Create a new debug session for a given script\n"+
"   * @param string the script name\n"+
"   */\n"+
"  public function pdb_Session($scriptName) {\n"+
"	$this->breakpoints = $this->lines = array();\n"+
"	$this->currentTopLevelFrame = $this->currentFrame = new pdb_Environment(null, $scriptName, true);\n"+
"	$this->allFrames[] = $this->currentFrame;\n"+
"\n"+
"	$this->currentView = null;\n"+
"  }\n"+
"  /**\n"+
"   * Return the clickable HTML script source, either from the cusom view or from the current frame\n"+
"   * @return string the HTML script source\n"+
"   */\n"+
"  public function getCurrentViewHtmlScriptSource () {\n"+
"	return $this->currentView ? $this->currentView->getHtmlScriptSource() : $this->currentFrame->getHtmlScriptSource();\n"+
"  }   \n"+
"  /**\n"+
"   * Return the current frame script name\n"+
"   * @return string the script name of the current frame\n"+
"   */\n"+
"  public function getScriptName () {\n"+
"	return $this->currentFrame->scriptName;\n"+
"  }\n"+
"  /**\n"+
"   * Return the current script name, either from the view or from the current frame\n"+
"   * @return string the current script name\n"+
"   */\n"+
"  public function getCurrentViewScriptName () {\n"+
"	return $this->currentView ? $this->currentView->scriptName : $this->getScriptName();\n"+
"  }   \n"+
"  /**\n"+
"   * Return the breakpoints for the current script\n"+
"   * @return object the breakpoints\n"+
"   */\n"+
"  public function getBreakpoints () {\n"+
"	$bp = array();\n"+
"	foreach ($this->breakpoints as $breakpoint) {\n"+
"	  if ($this->getCurrentViewScriptName() != $breakpoint->scriptName) continue;\n"+
"	  array_push($bp, $breakpoint->breakpoint);\n"+
"	}\n"+
"	return $bp;\n"+
"  }\n"+
"  /**\n"+
"   * toggle and write breakpoint reply\n"+
"   * @param object the current comm. channel\n"+
"   * @param object the breakpoint\n"+
"   */\n"+
"  public function toggleBreakpoint($breakpoint) {\n"+
"	$id = $breakpoint.\"@\".$this->getCurrentViewScriptName();\n"+
"	if (!isset($this->breakpoints[$id])) {\n"+
"	  $this->breakpoints[$id] = new pdb_Breakpoint($breakpoint, $this->getCurrentViewScriptName(), substr($breakpoint, 3));\n"+
"	  return false;\n"+
"	} else {\n"+
"	  $bp = $this->breakpoints[$id];\n"+
"	  unset ($this->breakpoints[$id]);\n"+
"	  return $bp;\n"+
"	}\n"+
"  }\n"+
"  /**\n"+
"   * check if there's a breakpoint\n"+
"   * @param string the script name\n"+
"   * @param int the line within the script \n"+
"   * @return true if a breakpoint exists at line, false otherwise\n"+
"   */\n"+
"  public function hasBreakpoint($scriptName, $line) {\n"+
"	if ($this->currentFrame->stepNext) return true;\n"+
"  \n"+
"	foreach ($this->breakpoints as $breakpoint) {\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"process breakpoint::: $scriptName, $line:: $breakpoint\");\n"+
"	  if($breakpoint->type==1) {\n"+
"		if ($breakpoint->scriptName==$scriptName&&$breakpoint->line==$line) return true;\n"+
"	  }\n"+
"	}\n"+
"	return false;\n"+
"  }\n"+
"  /**\n"+
"   * parse code\n"+
"   * @param string the script name\n"+
"   * @param string the content\n"+
"   * @return the parsed script\n"+
"   */\n"+
"  public function parseCode($scriptName, $content) {\n"+
"	$parser = new pdb_Parser($scriptName, $content);\n"+
"	return $parser->parseScript();\n"+
"  }\n"+
"  private static function doEval($__pdb_Code) {\n"+
"	return eval (\"?>\".$__pdb_Code);\n"+
"  }\n"+
"  /**\n"+
"   * parse and execute script\n"+
"   * @return the script output\n"+
"   */\n"+
"  public function evalScript() {\n"+
"	$code = $this->parseCode($this->getScriptName(), file_get_contents($this->getScriptName()));\n"+
"\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"eval:::$code,\".$this->getScriptName().\"\\n\");\n"+
"	ob_start();\n"+
"	self::doEval ($code);\n"+
"	$this->output = ob_get_contents();\n"+
"	ob_end_clean();\n"+
"\n"+
"	return $this->output;\n"+
"  }\n"+
"}\n"+
"\n"+
"/**\n"+
" * The java script debugger server daemon. Contains a debug session\n"+
" * and handles debug requests from the client.\n"+
" * @access private\n"+
" */\n"+
"class pdb_JSDebugger {\n"+
"  /** The pdb_Session */\n"+
"  public $session;\n"+
"  private $id;\n"+
" \n"+
"  public $end;\n"+
"  private $includedScripts;\n"+
"  private $conn;\n"+
"  private $ignoreInterrupt;\n"+
"\n"+
"  const STEP_INTO = 1;\n"+
"  const STEP_OVER = 2;\n"+
"  const STEP_OUT = 3;\n"+
"  const GO    = 4;\n"+
"\n"+
"\n"+
"  private function getConnection($id) {\n"+
"	return new pdb_PollingServerConnection($id);\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * Create new PHP debugger using a given comm. ID\n"+
"   * @param int the communication address\n"+
"   */\n"+
"  public function pdb_JSDebugger($id) {\n"+
"\n"+
"	$this->id = $id;\n"+
"	$this->conn = $this->getConnection($id);\n"+
"\n"+
"	$this->end = false;\n"+
"	$this->session = null;\n"+
"\n"+
"	$this->includedScripts = array();\n"+
"\n"+
"	$this->ignoreInterrupt = false;\n"+
"	set_error_handler(\"pdb_error_handler\");\n"+
"	register_shutdown_function(\"pdb_shutdown\");\n"+
"  }\n"+
"  public function setError($errno, $errfile, $errline, $errstr) {\n"+
"	highlight_string(\"PHP error $errno: $errstr in $errfile line $errline\");\n"+
"  }\n"+
"  /**\n"+
"   * Return the current comm. ID\n"+
"   * @return int the communication address\n"+
"   */\n"+
"  public function getServerID() {\n"+
"	return $this->id;\n"+
"  }\n"+
"  /**\n"+
"   * Read data from the front end\n"+
"   * @return object the data \n"+
"   */\n"+
"  public function read() {\n"+
"	return $this->conn->read();\n"+
"  }\n"+
"  /**\n"+
"   * Write data to the front end\n"+
"   * @param object the data\n"+
"   */\n"+
"  public function write($data) {\n"+
"	$data[\"serverID\"] = $this->getServerID();\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"->\".print_r($data, true));\n"+
"	return $this->conn->write($data);\n"+
"  }\n"+
"  private function ack() {\n"+
"	$this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"					   \"seq\"=>$this->packet->seq));\n"+
"  }\n"+
"\n"+
"  private function getOutput() {\n"+
"	if (!$this->session) return \"\";\n"+
"\n"+
"	if (!$this->end) $output = $this->session->output = ob_get_contents();\n"+
"	return $this->session->output;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * Handle requests from the front end\n"+
"   */\n"+
"  public function handleRequests() {\n"+
"	$this->ignoreInterrupt = false;\n"+
"\n"+
"	while(!$this->end) {\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"handleRequests: accept\");\n"+
"   \n"+
"	  if (!($this->packet = $this->read())) break; // ignore __destructors after shutdown\n"+
"\n"+
"	  if (PDB_DEBUG) pdb_Logger::debug(\"handleRequests: done accept \".$this->packet->cmd);\n"+
"\n"+
"	  switch($this->packet->cmd) {\n"+
"	  case \"status\":\n"+
"		$this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"						   \"seq\"=>$this->packet->seq, \n"+
"						   \"line\"=>$this->session->currentFrame->line, \n"+
"						   \"scriptName\"=>$this->session->getCurrentViewScriptName(), \n"+
"						   \"breakpoints\"=>$this->session->getBreakpoints()));\n"+
"		break;\n"+
"	  case \"extendedStatus\":\n"+
"		$this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"						   \"seq\"=>$this->packet->seq, \n"+
"						   \"line\"=>$this->session->currentFrame->line, \n"+
"						   \"scriptName\"=>$this->session->getCurrentViewScriptName(), \n"+
"						   \"script\"=>$this->session->getCurrentViewHtmlScriptSource(),\n"+
"						   \"breakpoints\"=>$this->session->getBreakpoints()));\n"+
"		break;\n"+
"	  case \"begin\":\n"+
"		chdir (urldecode($this->packet->cwd));\n"+
"		$this->session = new pdb_Session(urldecode($this->packet->scriptName));\n"+
"		$this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"						   \"seq\"=>$this->packet->seq, \n"+
"						   \"scriptName\"=>$this->packet->scriptName, \n"+
"						   \"script\"=>$this->session->getCurrentViewHtmlScriptSource()));\n"+
"\n"+
"		$this->session->evalScript();\n"+
"		$this->end = true;\n"+
"		break;\n"+
"	  case \"stepNext\":\n"+
"		if ($this->end) break;\n"+
"		$this->session->currentView = null;\n"+
"		$this->ack();\n"+
"		return self::STEP_INTO;\n"+
"	  case \"stepOver\":\n"+
"		if ($this->end) break;\n"+
"		$this->session->currentView = null;\n"+
"		$this->ack();\n"+
"		return self::STEP_OVER;\n"+
"	  case \"go\":\n"+
"		if ($this->end) break;\n"+
"		$this->session->currentView = null;\n"+
"		$this->ack();\n"+
"		return self::GO;\n"+
"	  case \"stepOut\":\n"+
"		if ($this->end) break;\n"+
"		$this->session->currentView = null;\n"+
"		$this->ack();\n"+
"		return self::STEP_OUT;\n"+
"	  case \"toggleBreakpoint\":\n"+
"		$bp = $this->session->toggleBreakpoint($this->packet->breakpoint);\n"+
"		$this->write($bp ? \n"+
"					 (array(\"cmd\"=>\"unsetBreakpoint\", \n"+
"							\"seq\"=>$this->packet->seq,\n"+
"							\"scriptName\"=>$bp->scriptName, \n"+
"							\"breakpoint\"=>$bp->breakpoint)) :\n"+
"					 (array(\"cmd\"=>\"setBreakpoint\", \n"+
"							\"seq\"=>$this->packet->seq,\n"+
"							\"scriptName\"=>$this->session->getCurrentViewScriptName(), \n"+
"							\"breakpoint\"=>$this->packet->breakpoint)));\n"+
"		break;\n"+
"	  case \"toolTip\":\n"+
"		$name = urldecode($this->packet->item);\n"+
"		$value = \"\";\n"+
"		if ($name[0]=='$') {\n"+
"		  $idx = substr($name, 1);\n"+
"		  $env = (object) $this->session->currentFrame->vars;\n"+
"		  $code = \"return \\$env->$idx;\";\n"+
"		  $value = eval($code);\n"+
"		  if (is_object($value)) {\n"+
"			$value = get_class($value) . \" object\";\n"+
"		  } elseif (is_array($value)) {\n"+
"			$value = \"array[\".count($value).\"]\";\n"+
"		  } elseif (!isset($value)) {\n"+
"			$value = \"<undefined>\";\n"+
"		  } else {\n"+
"			$value = print_r($value, true);\n"+
"		  }\n"+
"		} else {\n"+
"		  $value = $this->packet->item;\n"+
"		}\n"+
"		$this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"						   \"seq\"=>$this->packet->seq,\n"+
"						   \"item\"=>$this->packet->item,\n"+
"						   \"value\"=>$value));\n"+
"		break;\n"+
"	  case \"switchView\":\n"+
"		if (PDB_DEBUG) pdb_Logger::debug(\"switchView here\");\n"+
"		$name = urldecode($this->packet->scriptName);\n"+
"		if (PDB_DEBUG) pdb_Logger::debug(\"switchView $name\");\n"+
"		if ($name[0]=='$') {\n"+
"		  $idx = substr($name, 1);\n"+
"		  $env = (object) $this->session->currentFrame->vars;\n"+
"		  $code = \"return \\$env->$idx;\";\n"+
"\n"+
"		  $pdb_dbg->end = true;\n"+
"		  $value = eval($code);\n"+
"		  $pdb_dbg->end = false;\n"+
"\n"+
"		  $this->session->currentView = new pdb_VariableView($this->session->currentView, $name, $value);\n"+
"		} else {\n"+
"		  $this->end = true;\n"+
"		  $value = self::resolveIncludePath(eval(\"return ${name};\"));\n"+
"		  $this->end = false;\n"+
"\n"+
"		  $this->session->currentView = new pdb_View($this->session->currentView, realpath($value));\n"+
"		}\n"+
"		$this->ack();\n"+
"		break;\n"+
"	  case \"backView\":\n"+
"		if ($this->session->currentView)\n"+
"		  $this->session->currentView = $this->session->currentView->parent;\n"+
"		$this->ack();\n"+
"		break;\n"+
"	  case \"output\":\n"+
"		if ($this->session) {\n"+
"		  $this->write(array(\"cmd\"=>$this->packet->cmd,\n"+
"							 \"seq\"=>$this->packet->seq, \n"+
"							 \"output\"=>$this->getOutput()));\n"+
"		} else {\n"+
"		  $this->ack();\n"+
"		}\n"+
"		break;\n"+
"	  case \"end\":\n"+
"		$this->end();\n"+
"		break;\n"+
"	  default:\n"+
"		if (PDB_DEBUG) pdb_Logger::debug(\"illegal packet: \" . print_r($this->packet, true));\n"+
"		exit(1);\n"+
"	  }\n"+
"	}\n"+
"	return self::GO;\n"+
"  }\n"+
"  public function end() {\n"+
"	$this->session->currentView = null;\n"+
"	$this->write(array(\"cmd\"=>\"end\",\n"+
"					   \"seq\"=>$this->packet->seq, \n"+
"					   \"output\"=>$this->getOutput()));\n"+
"	$this->end = true;\n"+
"  }\n"+
"  /**\n"+
"   * shut down the current comm. channel\n"+
"   */\n"+
"  public function shutdown() {\n"+
"	$this->conn->setOutput($this->getOutput());\n"+
"	$this->conn->shutdown();\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * called at run-time for each frame\n"+
"   * @return the current frame\n"+
"   */\n"+
"  public function startCall($scriptName) {\n"+
"	/* check for stepOver and stepOut */\n"+
"	$stepNext = $this->session->currentFrame->stepNext == pdb_JSDebugger::STEP_INTO ? pdb_JSDebugger::STEP_INTO : false;\n"+
"	\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"startCall::$scriptName, $stepNext\");\n"+
"	$env = new pdb_Environment($this->session->currentFrame, $scriptName, $stepNext);\n"+
"	$this->session->allFrames[] = $env;\n"+
"	return $env;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * @access private\n"+
"   */\n"+
"  protected function resolveIncludePath($scriptName) {\n"+
"	if (file_exists($scriptName)) return realpath($scriptName);\n"+
"\n"+
"	$paths = explode(PATH_SEPARATOR, get_include_path());\n"+
"	$name = $scriptName;\n"+
"	foreach ($paths as $path) {\n"+
"	  $x = substr($path, -1);\n"+
"	  if ($x != \"/\" && $x != DIRECTORY_SEPARATOR) $path.=DIRECTORY_SEPARATOR;\n"+
"	  $scriptName = realpath(\"${path}${name}\");\n"+
"	  if ($scriptName) return $scriptName;\n"+
"	}\n"+
"	trigger_error(\"file $scriptName not found\", E_USER_ERROR);\n"+
"  }\n"+
"  /**\n"+
"   * called at run-time for each included file\n"+
"   * @param string the script name\n"+
"   * @return string the code\n"+
"   */\n"+
"  public function startInclude($scriptName, $once) {\n"+
"	$isDebugger = (basename($scriptName) == \"PHPDebugger.php\");\n"+
"	if (!$isDebugger)\n"+
"	  $scriptName = $this->resolveIncludePath($scriptName);\n"+
"\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"scriptName::$scriptName, $isDebugger\");\n"+
"\n"+
"	if ($once && isset($this->includedScripts[$scriptName]))\n"+
"	  $isDebugger = true;\n"+
"\n"+
"	// include only from a top-level environment\n"+
"	// initial line# and vars may be wrong due to a side-effect in step\n"+
"	$this->session->currentFrame = $this->session->currentTopLevelFrame;\n"+
"\n"+
"	$stepNext = $this->session->currentFrame->stepNext == pdb_JSDebugger::STEP_INTO ? pdb_JSDebugger::STEP_INTO : false;\n"+
"	$this->session->currentFrame = new pdb_Environment($this->session->currentFrame, $scriptName, $stepNext);\n"+
"	$this->session->allFrames[] = $this->session->currentFrame;\n"+
"\n"+
"	if ($isDebugger) // do not debug self\n"+
"	  $code = \"<?php ?>\";\n"+
"	else\n"+
"	  $code = $this->session->parseCode(realpath($scriptName), file_get_contents($scriptName));\n"+
"\n"+
"	$this->session->currentTopLevelFrame = $this->session->currentFrame;\n"+
"\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"startInclude:::\".$this->session->currentTopLevelFrame . \" parent: \" . $this->session->currentTopLevelFrame->parent . \" code: \".$code);\n"+
"\n"+
"	if ($once) $this->includedScripts[$scriptName] = true;\n"+
"	return $code;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * called at run-time after the script has been included\n"+
"   */\n"+
"  public function endInclude() {\n"+
"	if (PDB_DEBUG) pdb_Logger::debug(\"endInclude:::\".$this->session->currentTopLevelFrame . \"parent: \".$this->session->currentTopLevelFrame->parent);\n"+
"\n"+
"	$this->session->currentFrame = $this->session->currentTopLevelFrame = \n"+
"	  $this->session->currentTopLevelFrame->parent;\n"+
"  }\n"+
"\n"+
"  /**\n"+
"   * called at run-time for each line\n"+
"   * @param string the script name\n"+
"   * @param int the current line\n"+
"   * @param mixed the execution variables\n"+
"   */\n"+
"  public function step($scriptName, $line, $vars) {\n"+
"	if ($this->ignoreInterrupt) return; // avoid spurious step calls from __destruct() method\n"+
"	$this->ignoreInterrupt = true;\n"+
"\n"+
"	if (PDB_DEBUG) pdb_Logger::logDebug(\"step: $scriptName @ $line\");\n"+
"	// pull the current frame from the stack or the top-level environment\n"+
"	$this->session->currentFrame = (isset($vars['__pdb_CurrentFrame'])) ? $vars['__pdb_CurrentFrame'] : $this->session->currentTopLevelFrame;\n"+
"	unset($vars['__pdb_CurrentFrame']);\n"+
"\n"+
"	$this->session->currentFrame->update($line, $vars);\n"+
"\n"+
"	if ($this->session->hasBreakpoint($scriptName, $line)) {\n"+
"	  $stepNext = $this->handleRequests();\n"+
"	  if (PDB_DEBUG) pdb_Logger::logDebug(\"continue\");\n"+
"\n"+
"	  /* clear all dynamic breakpoints */\n"+
"	  foreach ($this->session->allFrames as $currentFrame)\n"+
"		$currentFrame->stepNext = false;\n"+
"\n"+
"	  /* set new dynamic breakpoint */\n"+
"	  if ($stepNext != pdb_JSDebugger::GO) {\n"+
"		$currentFrame = $this->session->currentFrame;\n"+
"\n"+
"		/* break in current frame or frame below */\n"+
"		if ($stepNext != pdb_JSDebugger::STEP_OUT)\n"+
"		  $currentFrame->stepNext = $stepNext;\n"+
"\n"+
"		/* or break in any parent */\n"+
"		while ($currentFrame = $currentFrame->parent) {\n"+
"		  $currentFrame->stepNext = $stepNext;\n"+
"		}\n"+
"	  }\n"+
"	}\n"+
"\n"+
"	$this->ignoreInterrupt = false;\n"+
"	if (PDB_DEBUG) pdb_Logger::logDebug(\"endStep: $scriptName @ $line\");\n"+
"  }\n"+
"}\n"+
"\n"+
"/**\n"+
" * Convenience function called by the executor\n"+
" * @access private\n"+
" */\n"+
"function pdb_getDefinedVars($vars1, $vars2) {\n"+
"  if(isset($vars2)) $vars1['pbd_This'] = $vars2;\n"+
"\n"+
"  unset($vars1['__pdb_Code']);	   // see pdb_Message::doEval()\n"+
"\n"+
"  return $vars1;  \n"+
"}\n"+
"\n"+
"/**\n"+
" * Convenience function called by the executor\n"+
" * @access private\n"+
" */\n"+
"function pdb_startCall($scriptName, $line) {\n"+
"  global $pdb_dbg;\n"+
"  if (isset($pdb_dbg)) return $pdb_dbg->startCall($scriptName);\n"+
"}\n"+
"\n"+
"/**\n"+
" * Convenience function called by the executor\n"+
" * @access private\n"+
" */\n"+
"function pdb_startInclude($scriptName, $once) {\n"+
"  global $pdb_dbg;\n"+
"  if (isset($pdb_dbg)) return $pdb_dbg->startInclude($scriptName, $once);\n"+
"  else return \"\";\n"+
"}\n"+
"\n"+
"/**\n"+
" * Convenience function called by the executor\n"+
" * @access private\n"+
" */\n"+
"function pdb_endInclude() {\n"+
"  global $pdb_dbg;\n"+
"  if (isset($pdb_dbg)) $pdb_dbg->endInclude();\n"+
"}\n"+
"\n"+
"/**\n"+
" * Convenience function called by the executor\n"+
" * @access private\n"+
" */\n"+
"function pdb_step($scriptName, $line, $vars) {\n"+
"  global $pdb_dbg;\n"+
"  if (isset($pdb_dbg)) $pdb_dbg->step($scriptName, $line, $vars);\n"+
"}\n"+
"\n"+
"/**\n"+
" * @access private\n"+
" */\n"+
"function pdb_error_handler($errno, $errstr, $errfile, $errline) {\n"+
"  global $pdb_dbg;\n"+
"  if (PDB_DEBUG) pdb_Logger::debug(\"PHP error $errno: $errstr in $errfile line $errline\");\n"+
"  if ($pdb_dbg->end) return true;\n"+
" \n"+
" if (strncmp(basename($errfile),\"PHPDebugger\", 11)) \n"+
"   $pdb_dbg->setError($errno, $errfile, $errline, $errstr);\n"+
"\n"+
"  return true;\n"+
"}\n"+
"\n"+
"/**\n"+
" * @access private\n"+
" */\n"+
"function pdb_shutdown() {\n"+
"  global $pdb_dbg;\n"+
"  if (PDB_DEBUG) pdb_Logger::debug(\"PHP error: \".print_r(error_get_last(), true));\n"+
"  if ($pdb_dbg->end) return;\n"+
"\n"+
"  $error = error_get_last();\n"+
"  if ($error) {\n"+
"	$pdb_dbg->setError($error['type'], $error['file'], $error['line'], $error['message']);\n"+
"	$pdb_dbg->end();\n"+
"	$pdb_dbg->shutdown();\n"+
"  }\n"+
"}\n"+
"\n"+
"if (PDB_DEBUG==2) {\n"+
"  $parser = new pdb_Parser($argv[1], file_get_contents($argv[1]));\n"+
" echo $parser->parseScript();\n"+
" exit (2);\n"+
"}\n"+
"\n"+
"/* * The JavaScript part, invoked after the debugger has been included() * */\n"+
"if (!isset($_SERVER['HTTP_XPDB_DEBUGGER'])) {\n"+
"\n"+
"  session_start();\n"+
"  header(\"Expires: Sat, 1 Jan 2005 00:00:00 GMT\");\n"+
"  header(\"Last-Modified: \".gmdate( \"D, d M Y H:i:s\").\" GMT\");\n"+
"  header(\"Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0\");\n"+
"  header(\"Pragma: no-cache\");\n"+
"  header(\"Content-Type: text/html\");\n"+
"?>\n"+
"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"+
"<html>\n"+
"<head>\n"+
"<title>\n"+
"PHPDebugger version 1.0\n"+
"</title>\n"+
"<style type=\"text/css\">\n"+
"#tooltip {\n"+
"  background:#EFEFEF none repeat scroll 0 0;\n"+
"  height:auto;\n"+
"  width:auto;\n"+
"  min-width: 1px;\n"+
"  min-height: 1.5ex;\n"+
"  display: block;\n"+
"  border:1px solid black;\n"+
"  background-color:gray; color:white;\n"+
"  position:absolute;\n"+
"  text-align: center;\n"+
"  z-index:75;\n"+
"}\n"+
".tt {\n"+
"  visibility: hidden;\n"+
"}\n"+
".ttHover {\n"+
"  visibility: visible;\n"+
"}\n"+
"\n"+
"#run {\n"+
"  height: 13px;\n"+
"  width: 17px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  background:green url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABEAAAANCAYAAABPeYUaAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwcXGXdF1DwAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABEUlEQVQoz62SoWvDUBDGfx0Rry6VzzUyg4lEVgYmRulfMVUKVWOqTFVWDULVbNxsTSETg8jOzm+QwcSL28ECmwhJGpqwwnrmfXfv7nvf3b2e+TQ//NMsgPRlQfoeQ14E9TBAe0uC9QyAeBoeQfK6IZjuqmC89tHeEoDFxP+TzKqQJK0JksPNlQ/QSWYBiJQVKShd+4DJa+f68hyA0f0M7fR5nKyaSiQTlHwgatB4JXkzB+q8C12RJfNwr522Vr4hy7PjtgOgbAWZKs6SRASt+gfZ8bPBcQck83B/Jl/Fre3S8AGlarx5Kuazu33o2I4atUq1LYi27cUNEj0cE925VdBxxxWOttJZXFrvFN/+jBPYL4uJYFb2zCiIAAAAAElFTkSuQmCC') no-repeat;\n"+
"}\n"+
"#terminate {\n"+
"  height: 13px;\n"+
"  width: 13px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  background:red url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYrNECrm4EAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAAwUlEQVQoz52SLQ7CQBCFvyaI4nYlciWSI3CEFkVQGAyOcBU0ClCARCKRHGFlN0HsuK4D0RRasWnKJOPel/fmJ/Ev/6ZnDQD89YzcL51iNc3R2ayC5HZkslx3Qs/97gcBEEBOh7jLfNGOh5RY8egQd7HiQcoG9HULPRZRwthagvdR4chapBUPQELVsZKAL8HUkBZApVVHN5FWuhoKzlEYg06HUaYwhuBcO55RGjbxWynANWdSWc5jte3+iCwHIPnn9z7J30SR7ayFNQAAAABJRU5ErkJggg==') no-repeat;\n"+
"}\n"+
"#stepInto {\n"+
"  height: 13px;\n"+
"  width: 17px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  background: yellow url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABEAAAANCAYAAABPeYUaAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYtD6f61SMAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAA6ElEQVQoz2N8//r9fwYKAQuMcWy6JlYFmq4lDIIqyYQNOTZdk8Gr8jpWBdvaNRkUPz9nkDSswWkII8w72Fyi6JjMoGmRw7Ct3ZDBKvM6Ye9gU3RsuiaDpkkywTBhIqjizwfiAxbdBQhDfqCIYXMxVkN+fGdgCCqaCzXkPYNXJoS9ri+ZeO84FV1nWNYUDfEKFC9rimZwKrpOWph41d1nWNaWzcDw5zvDsrZsBq+6+/ijOHn+MQbBzxBF73kVGeYmWiHSSZMiigHY1LIwMDAw/Di/lOHaC4giQYljDAxIhqC7AJtaRmrkHQBYX2Q4HZvQSwAAAABJRU5ErkJggg==') no-repeat;\n"+
"}\n"+
"#stepOver {\n"+
"  height: 13px;\n"+
"  width: 17px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  background: yellow url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABEAAAANCAYAAABPeYUaAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYsOAZcQW0AAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABSklEQVQoz62TP0jDQBTGv5QMdSgYcEjGgEvHdsyYsW6Kg27SKeJki1PpEAShqJPYKTilHUS3dOyYbu1mhpZmERqIcIEOyRA4h9qaeCcK+uA43uPj9/7cO4GEhOKPJmYdMrMQTJ5A3nxGKO2okCsHkHbr30PIzII/slA9vMO2XGWEUTDG+PEMyXIBpdJiIWuAbjiIXkcYXJUZiHZ0A91wMOzuoVhS8hWRkFDHlCmN55RMbeqYMiUhYY5jypRMbUrjOaMpbGhpBLffgGZ43OFphge33wDSiD+TJAaQJqubY243016a5GKa4UFYP/HAVFFr+1zI8LaM/dNLoKh8BpMFnu9b0M8zkJ9sYKo4vrgGxC0gjdHrNDdJC79dqFrbR6/TZAAA+JXUH1xIy5WIlFRYJ1quoq9ti7ysycTGS/CxqbILZCC8uQn/8XfeAUbdtbmYIYl8AAAAAElFTkSuQmCC') no-repeat;\n"+
"}\n"+
"#stepOut {\n"+
"  height: 13px;\n"+
"  width: 17px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  background: yellow url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABEAAAANCAYAAABPeYUaAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYsF62NfDQAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABAUlEQVQoz62Tv2vCQBTHP5GsDoJDV8f8C44ZM+Y/EOnSgoOCQ4YOxVE6BH9MoZMdM3TI6CLcJNzmLULGCg4p6HBgoB0SFH9gKvYtj7v3fR/ufe/OSNbJD3dG6RaxGFuIsXUfhB04j0OEfwwyi/q+ZI94NjlsVG2cpyGRb1FvKQCMa54kywAZ9XE9eV78FoSjZ+yWun4S+dnH9aao+QAVBQC43hS2ktDvYLfVXz3RqCjYN5wCiiEpkOos5+vw7RhQbGyqIU2yDNhddfs70TlIFwxsAjTfBZVNnN1IuUbQqJ+i9nFJawJoOWGxykSVBwE5xPFiPl5rOC/xAXlBa/zH3/kFlslu4rTXaTUAAAAASUVORK5CYII=') no-repeat;\n"+
"}\n"+
"#output {\n"+
"  height: 13px;\n"+
"  width: 13px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  visibility: visible;\n"+
"  background: blue url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLCQYlB4EnoCoAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABCklEQVQoz42Rv0oDQRCHv4MJbCCFgoUHvkBKXyGPEDufwUokhdFGokWwt7IRbISkTHmvcIUPcUKK3eJgp1g4i92Ld4gxWw2z329+8ye7vF81pcs59J0fVUjpcsZaHCwq3QQBD8Ddcv6vYDF7BDxCACMGgPFov8iIgQACChKTVw9rvCrqPK52+FopPjodCBA0Og1H0el5Nv1VXcNPPDQGHAhBMcnpZrlOJFi1aK1orXhVNm/zxGnbXnRaXE//nEchcm17ZpB+ggImIaaDx9gMaBfhQeD95bVfWiLQi4W48vzkmM1ndfBx89OczG5t001ePBVUX1UPWt1OesLMbm2zA4Puhk3n7LucxQLfZ4Norb3ftQMAAAAASUVORK5CYII=') no-repeat;\n"+
"}\n"+
"#backView {\n"+
"  height: 13px;\n"+
"  width: 16px;\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  margin: 1px 10px 1px 10px;\n"+
"  visibility: visible;\n"+
"  background: yellow url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAANCAYAAACgu+4kAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwc3GnvIoSQAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABH0lEQVQoz5WToU4DQRCGvzZLspUn+wpIJLLyJDiQEBLMJZDAA1QgTlRUU1tXCY+A5BGoPHFiKkhukm4yiD3CNr3S9k8mu/l3Z+af2dme1GJ04HM6hBPP2f2S/9DvIj/KjNG4gUbZh36Xc15GURr2B3Abzi8Z+cQABTyqkdu8BTjP+WMV91KLSS22eMDMGjOrzNZfZuvKzCSxJlnN3p4wqcVclD3kYlJBWEJQCACrJLNvVwXnwWVoSErQbwUEwgq0+ZMa2JYfBuAV1aSJo7EwvzsFmpj515y2/WjNKbim5SN66Ry8P2dcTRexDGBWXDNwHSoc5GPZfoW8FGZFxs30FYCBi9xRc3BZCrPiNoZ2/rg5SIPMi+GO0030dv2FQ/EDM7CYPt7DsBMAAAAASUVORK5CYII=') no-repeat;\n"+
"}\n"+
" \n"+
".normal {\n"+
"  background: transparent;\n"+
"}\n"+
".selected {\n"+
"  background: yellow url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYQB8PJFa8AAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAABNElEQVQoz4WPsUsCYRjGf8YNNzTo7vJBDgUNHTSc4KBbro6ORy5KU5tN0VhjS+FfEDXaEFxbY9cQuQSfQ3BCwyck3At9YIOieZo98MAL7/vjed6MMWYMcH6aQxUqePsBaqvKOm0A6PcQpaDoh4QPdcL7E8xQr4EsmC9BLKg8NA8hm72kc+Xx9Hi2GhIryMgAIHZibw9aDZDRBZ3rIs9RZ7neTDK3C1RKUC336L0cc3tTR/fDFGTByLLdTagegMp36d7VAHAWQuSPzy0Mh5BM987vxSoojiGKQBUCgkYzBQGSzOdkBNEr4Pj45QBvtza7XkhCILGg+6A1FEttPL+F67oL6RPIcZFveJse72wHBEdtctncyhcz5tOM40GM1iHxR4jnN1F5L1U8DRkzFhGwTGo4/KsfXjWDCQ6TmRMAAAAASUVORK5CYII=') no-repeat;\n"+
"}\n"+
".breakpointSet {\n"+
"  background: red url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9kLBwYOGJqAJ4UAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAA+0lEQVQoz5XSMUvDUBSG4bcXhyM4pFsudBE6GEfB5UKGZLOjq5uCIE7iPyjdxEXQoZC/4OgipIOQbqZDMW6OcbsZBO8Q0MEWkWJJ3vl7lsPpWGu/WOQqy8s44X2S8ukcmyL4Uczu6QnidZczOktUPqY8HB/Bh2OlLWEwTtAHg19kXwvuQwM1/7cBh08Z3Z0ABZCena8HAPViB6hymlHNcppUzXLKaYaq5gVtquYFitq1QtQO5fWDVsbrBygdGqTnNwLS89GhQSFCfJc0QvHNLYj8nFyHBjMcrQVmOEJH8d+PALD5M/nVNW+TFJwDEbajmODiEr2/t/pGbfoGcP1aToLr9OAAAAAASUVORK5CYII=') no-repeat;\n"+
"}\n"+
"\n"+
"#navigationBar {\n"+
"  display: block;\n"+
"  position: fixed;\n"+
"  top: 0px;\n"+
"  left: 0px;\n"+
"  height: 20px;\n"+
"  width: 100%;\n"+
"  z-index: 100;\n"+
"  background: #efefef;\n"+
"}\n"+
"#code {\n"+
"  display: block;\n"+
"  position: absolute;\n"+
"  left: 92px;\n"+
"  top: 22px;\n"+
"  z-index: 50;\n"+
"}\n"+
"\n"+
".breakpoint {\n"+
"  display: inline;\n"+
"  position: absolute;\n"+
"  left: -92px;\n"+
"  width: 90px;\n"+
"  height: 13px;\n"+
"  background: #efefef;\n"+
"  overflow: hidden;\n"+
"}\n"+
"\n"+
".linenumber {\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  width: 59px;\n"+
"  height: 13px;\n"+
"  text-align: right;\n"+
"  margin-right: 5px;\n"+
"  float: right;\n"+
"  color: black;\n"+
"  \n"+
"}\n"+
".currentlineIndicator {\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  width: 13px;\n"+
"  height: 13px;\n"+
"  float: left;\n"+
"}\n"+
".breakpointIndicator {\n"+
"  display: inline-block;\n"+
"  position: relative;\n"+
"  width: 13px;\n"+
"  height: 13px;\n"+
"  float: left;\n"+
"}\n"+
"</style>\n"+
"\n"+
"<script type=\"text/javascript\">\n"+
"var http = createRequestObject();\n"+
"var httpCtrl = createRequestObject();\n"+
"var serverID = <?php echo pdb_JSDebuggerClient::getServerID(); ?>;\n"+
"var scriptName = \"<?php echo pdb_JSDebuggerClient::getDebugScriptName(); ?>\";\n"+
"var currentScriptName = \"\";\n"+
"var cwd = \"<?php echo urlencode(getcwd()); ?>\";\n"+
"var debuggerURL = \"<?php echo pdb_JSDebuggerClient::getDebuggerURL(); ?>\";\n"+
"var date = \"<?php echo gmdate( 'D, d M Y H:i:s').' GMT'; ?>\";\n"+
"var seq = 1;\n"+
"\n"+
"function getSeq() {\n"+
" return seq++;\n"+
"}\n"+
"function getServerID() {\n"+
"  return serverID;\n"+
"}\n"+
"function createRequestObject() {\n"+
"  var req;\n"+
"  var browser = navigator.appName;\n"+
"  if (browser == \"Microsoft Internet Explorer\") {\n"+
"	req = new ActiveXObject(\"Microsoft.XMLHTTP\");\n"+
"  } else {\n"+
"	req = new XMLHttpRequest();\n"+
"  }\n"+
"  return req;\n"+
"}\n"+
"function doCmd(text) {\n"+
"  switch(text.cmd) {\n"+
"  case 'stepNext': \n"+
"  case 'stepOver': \n"+
"  case 'go':\n"+
"  case 'switchView':\n"+
"  case 'backView':\n"+
"  case 'begin':\n"+
"  case 'stepOut':	getStatusCB(text); break;\n"+
"\n"+
"  case 'output':	showOutputCB(text); break;\n"+
"\n"+
"  case 'status':	showStatusCB(text); break;\n"+
"\n"+
"  case 'extendedStatus': showExtendedStatusCB(text); break;\n"+
"\n"+
"  case 'setBreakpoint':	setBreakpointCB(text); break;\n"+
"\n"+
"  case 'unsetBreakpoint':	unsetBreakpointCB(text); break;\n"+
"\n"+
"  case 'term':\n"+
"  case 'end':		endCB(text); break;\n"+
"  case 'toolTip':   showToolTipCB(text); break;\n"+
"\n"+
"  default: alert(\"illegal cmd: \" + text.cmd); break;\n"+
"  }\n"+
"}\n"+
"function hasClassName(element, className) {\n"+
"  var elementClassName = element.className;\n"+
"  if (elementClassName.length == 0) return false;\n"+
"  if (elementClassName == className ||\n"+
"    elementClassName.match(new RegExp(\"(^|\\\\s)\" + className + \"(\\\\s|$)\")))\n"+
"   return true;\n"+
"  return false;\n"+
"}\n"+
"if (document.getElementsByClassName == undefined) {\n"+
" document.getElementsByClassName = function(className) {\n"+
"	var children = document.body.getElementsByTagName('*');\n"+
"	var elements = [], child;\n"+
"	for (var i = 0, length = children.length; i < length; i++) {\n"+
"	 child = children[i];\n"+
"	 \n"+
"	 if (hasClassName(child, className)) {\n"+
"	elements.push(child);\n"+
"	 }\n"+
"	}\n"+
"	return elements;\n"+
" }\n"+
"}\n"+
"function sendCmd(cmd) {\n"+
"  var url = debuggerURL;\n"+
"  data = cmd+\"&serverID=\"+getServerID()+\"&seq=\"+getSeq();\n"+
"\n"+
"  /* use synchronous requests to avoid out of order execution of \n"+
"	 step -> status -> extended status sequences */\n"+
"  http.open(\"POST\", url, false);\n"+
"  http.setRequestHeader(\"Cache-Control\", \"no-store, no-cache, must-revalidate, post-check=0, pre-check=0\");\n"+
"  http.setRequestHeader(\"Last-Modified\", date);\n"+
"  http.setRequestHeader(\"Pragma\", \"no-cache\");\n"+
"  http.setRequestHeader(\"Expires\", \"Sat, 1 Jan 2005 00:00:00 GMT\");\n"+
"  http.setRequestHeader(\"Content-Type\", \"application/x-www-form-urlencoded\");\n"+
"  http.setRequestHeader(\"Content-Length\", data.length);\n"+
"  http.setRequestHeader(\"XPDB-DEBUGGER\", 0);\n"+
"  /*\n"+
"  http.onreadystatechange = function() {\n"+
"	 if(http.readyState == 4 && http.status == 200) {\n"+
"	   doCmd(eval(http.responseText));\n"+
"	 }\n"+
"  }\n"+
"  */\n"+
"\n"+
"  http.send(data);\n"+
"\n"+
"  doCmd(eval(http.responseText));\n"+
"}\n"+
"function startServer() { \n"+
"  var url = debuggerURL;\n"+
"	data = \"<?php echo pdb_JSDebuggerClient::getPostData(); ?>\";\n"+
"	method = \"<?php echo $_SERVER['REQUEST_METHOD']; ?>\";\n"+
"  httpCtrl.open(method, url, true);\n"+
"  httpCtrl.setRequestHeader(\"Cache-Control\", \"no-store, no-cache, must-revalidate, post-check=0, pre-check=0\");\n"+
"  httpCtrl.setRequestHeader(\"Last-Modified\", date);\n"+
"  httpCtrl.setRequestHeader(\"Pragma\", \"no-cache\");\n"+
"  httpCtrl.setRequestHeader(\"Expires\", \"Sat, 1 Jan 2005 00:00:00 GMT\");\n"+
"  httpCtrl.setRequestHeader(\"Content-Length\", data.length);\n"+
"  httpCtrl.setRequestHeader(\"Content-Type\", \"application/x-www-form-urlencoded\");\n"+
"  httpCtrl.setRequestHeader(\"XPDB-DEBUGGER\", getServerID());\n"+
"  httpCtrl.onreadystatechange = function() {\n"+
"	 if(httpCtrl.readyState == 4 && httpCtrl.status == 200) {\n"+
"	  //alert(\"debugger exited. Debugger debug output: \" +httpCtrl.responseText);\n"+
"	 }\n"+
"  }\n"+
"  httpCtrl.send(data);\n"+
"}\n"+
"function stepNext() {\n"+
"  sendCmd(\"cmd=stepNext\");\n"+
"}\n"+
"function stepOver() {\n"+
"  sendCmd(\"cmd=stepOver\");\n"+
"}\n"+
"function stepOut() {\n"+
"  sendCmd(\"cmd=stepOut\");\n"+
"}\n"+
"function getStatusCB(cmd) {\n"+
"	sendCmd(\"cmd=status\");\n"+
"}\n"+
"\n"+
"function loaded() {\n"+
"  startServer();\n"+
"  sendCmd(\"cmd=begin&scriptName=\"+encodeURI(scriptName)+\"&cwd=\"+encodeURI(cwd));\n"+
"}\n"+
"function toggleBreakpoint(el, event) {\n"+
"  sendCmd(\"cmd=toggleBreakpoint&breakpoint=\"+encodeURI(el.id));\n"+
"  return false;\n"+
"}\n"+
"function setBreakpointCB(cmd) {\n"+
"  document.getElementById(cmd.breakpoint).lastChild.className=\"breakpointIndicator breakpointSet\";\n"+
"}\n"+
"function unsetBreakpointCB(cmd) {\n"+
"  document.getElementById(cmd.breakpoint).lastChild.className=\"breakpointIndicator normal\";\n"+
"}\n"+
"function showOutputCB(cmd) {\n"+
"  currentScriptName = \"\";\n"+
"  document.getElementById(\"code\").innerHTML = cmd.output;\n"+
"  window.scrollTo(0, 0);\n"+
"}\n"+
"\n"+
"function doShowStatusCB(cmd) {\n"+
"  lines = document.getElementsByClassName(\"currentlineIndicator\");\n"+
"   if (lines.length == 0) {\n"+
"     window.scrollTo (0, 0);\n"+
"     return; // no lines to mark\n"+
"   }\n"+
"\n"+
"  lines = document.getElementsByClassName(\"currentlineIndicator selected\");\n"+
"  for (i=0; i<lines.length; i++) {\n"+
"	line = lines[i];\n"+
"	line.className = \"currentlineIndicator normal\";\n"+
"  }\n"+
"  for (i=0; i<cmd.breakpoints.length; i++) {\n"+
"	breakpoint = cmd.breakpoints[i];\n"+
"	document.getElementById(breakpoint).lastChild.className=\"breakpointIndicator breakpointSet\";\n"+
"  }\n"+
"  currentLine = document.getElementById(\"bp_\"+(cmd.line)).firstChild;\n"+
"\n"+
"	currentLine.className=\"currentlineIndicator selected\";\n"+
"\n"+
"	codeDiv = document.getElementById(\"code\");\n"+
"	codeLine = currentLine.parentNode;\n"+
"	toolsHeight = 30;\n"+
"	if (codeLine.offsetTop < window.pageYOffset || codeLine.offsetTop + codeLine.clientHeight +toolsHeight > window.pageYOffset+window.innerHeight) {\n"+
"	  window.scrollTo(0, codeLine.offsetTop + toolsHeight - window.innerHeight/2);\n"+
"	}\n"+
"}\n"+
"function showStatusCB(cmd) {\n"+
"	if (currentScriptName == cmd.scriptName)\n"+
"	 doShowStatusCB(cmd);\n"+
"	else\n"+
"	 sendCmd(\"cmd=extendedStatus\"); // another round-trip \n"+
"}\n"+
"function showExtendedStatusCB(cmd) {\n"+
"	currentScriptName = cmd.scriptName;\n"+
"	document.getElementById(\"code\").innerHTML = cmd.script;\n"+
"	document.title = cmd.scriptName;\n"+
"\n"+
"	doShowStatusCB(cmd);\n"+
"}\n"+
"\n"+
"function stepInto(el, event) {\n"+
"  sendCmd(\"cmd=stepNext\");\n"+
"  return false;\n"+
"}\n"+
"function stepOver(el, event) {\n"+
"  sendCmd(\"cmd=stepOver\");\n"+
"  return false;\n"+
"}\n"+
"function stepOut(el, event) {\n"+
"  sendCmd(\"cmd=stepOut\");\n"+
"  return false;\n"+
"}\n"+
"function end(el, event) {\n"+
"  sendCmd(\"cmd=end\");\n"+
"  return false;\n"+
"}\n"+
"function endCB(cmd) {\n"+
"  document.body.innerHTML = cmd.output;\n"+
"}\n"+
"function go(el, event) {\n"+
"  sendCmd(\"cmd=go\");\n"+
"  return false;\n"+
"}\n"+
"function switchView(data) {\n"+
"  data = encodeURI(data);\n"+
"  sendCmd(\"cmd=switchView&scriptName=\"+data);\n"+
"  return false;\n"+
"}\n"+
"function backView(el, event) {\n"+
"  sendCmd(\"cmd=backView\");\n"+
"  return false;\n"+
"}\n"+
"function output(el, event) {\n"+
"  sendCmd(\"cmd=output\");\n"+
"  return false;\n"+
"}\n"+
"\n"+
"var currentEl, tooltipItem;\n"+
"var currentX, currentY;\n"+
"var timer;\n"+
"var tt;\n"+
"function getToolTip() {\n"+
"  if(tt) return tt;\n"+
"  return tt = document.getElementById(\"tooltip\");\n"+
"}\n"+
"function mouseout(el, event) {\n"+
"  getToolTip().className = \"tt\";\n"+
"  if (timer) {\n"+
"	window.clearTimeout(timer);\n"+
"	timer = null;\n"+
"  }\n"+
"  return true;\n"+
"}\n"+
"function mousemove(el, event) {\n"+
"  if (timer) {\n"+
"	window.clearTimeout(timer);\n"+
"	getToolTip().className = \"tt\";\n"+
"	timer = null;\n"+
"  }\n"+
"  if (getEventSource(event).parentNode.className==\"breakpoint\") return true;\n"+
"  el = getEventSource(event);\n"+
"  if (el.nodeName !=\"SPAN\") return true;\n"+
"\n"+
"  currentEl = el;\n"+
"  currentX = getEventX(event) + window.pageXOffset;\n"+
"  currentY = getEventY(event) + window.pageYOffset;\n"+
"  timer = window.setTimeout(\"showToolTip()\", 600);\n"+
"  return false;\n"+
"}\n"+
"function toolTipCmd(el) {\n"+
"  el=encodeURI(el);\n"+
"  sendCmd(\"cmd=toolTip&item=\"+el);\n"+
"  return false;\n"+
"}\n"+
"function showToolTip() {\n"+
"  el = currentEl;\n"+
"  if (el && el.firstChild && el.firstChild.data) {\n"+
"	data = trim(el.firstChild.data);\n"+
"	while (el && el.previousSibling && el.previousSibling.firstChild && el.previousSibling.firstChild.data && trim(el.previousSibling.firstChild.data)=='->') {\n"+
"	  el = el.previousSibling.previousSibling;\n"+
"	  if (el && el.firstChild && el.firstChild.data) data = trim(el.firstChild.data)+\"->\"+data;\n"+
"	}\n"+
"  }\n"+
"    toolTipCmd(data);\n"+
"}\n"+
"function showToolTipCB(cmd) {\n"+
"  tt = getToolTip();\n"+
"  if(tooltipItem) tt.removeChild(tooltipItem);\n"+
"  tooltipItem = (document.createTextNode(cmd.value));\n"+
"  tt.appendChild(tooltipItem);\n"+
"  tt.style.top=(currentY+10) + \"px\";\n"+
"  tt.style.left=(currentX+10) + \"px\";\n"+
"  tt.className = \"ttHover\";\n"+
"\n"+
"}\n"+
"function trim(str) {\n"+
"  nbspChar = String.fromCharCode(160);\n"+
"  return str.replace(/^\\s*/, \"\").replace(/\\s*$/, \"\").replace(nbspChar,\"\");\n"+
"}\n"+
"function getEventSource(event) {\n"+
" if (event.target != undefined) return event.target;\n"+
" return event.srcElement;\n"+
"}\n"+
"function getEventX(event) {\n"+
"  if (event.clientX != undefined) return event.clientX;\n"+
"  return event.offsetX;\n"+
"}\n"+
"function getEventY(event) {\n"+
"  if (event.clientY != undefined) return event.clientY;\n"+
"  return event.offsetY;\n"+
"}\n"+
"function mousedown(el, event) {\n"+
"  getToolTip().className = \"tt\";\n"+
"\n"+
"  if (getEventSource(event).parentNode.className==\"breakpoint\") return true;\n"+
"  el = getEventSource(event);\n"+
"  if (el && el.firstChild && el.firstChild.data) {\n"+
"	data = trim(el.firstChild.data);\n"+
"	while (el && el.previousSibling && el.previousSibling.firstChild && el.previousSibling.firstChild.data && trim(el.previousSibling.firstChild.data)=='->') {\n"+
"	  el = el.previousSibling.previousSibling;\n"+
"	  if (el && el.firstChild && el.firstChild.data) data = trim(el.firstChild.data)+\"->\"+data;\n"+
"	}\n"+
"	switchView(data);\n"+
"  }\n"+
"  return false;\n"+
"}\n"+
"</script>\n"+
"\n"+
"</head>\n"+
"<body>\n"+
"\n"+
"<div id=\"navigationBar\">\n"+
"<a id=\"run\" onmousedown=\"return go(this, event);\"></a>\n"+
"<a id=\"terminate\" onmousedown=\"return end(this, event);\"></a>\n"+
"<a id=\"stepInto\" onmousedown=\"return stepInto(this, event);\"></a>\n"+
"<a id=\"stepOver\" onmousedown=\"return stepOver(this, event);\"></a>\n"+
"<a id=\"stepOut\" onmousedown=\"return stepOut(this, event);\"></a>\n"+
"<a id=\"output\" onmousedown=\"return output(this, event);\"></a>\n"+
"<a id=\"backView\" onmousedown=\"return backView(this, event);\"></a>\n"+
"</div>\n"+
" <div onmouseout=\"return mouseout(this, event);\" onmousemove=\"return mousemove(this, event);\" onmousedown=\"return mousedown(this, event);\">\n"+
" <div id=\"code\">\n"+
"<span>loading...\n"+
"<noscript>failed! Please enable JavaScript and try again.</noscript>\n"+
"</span>\n"+
"</div>\n"+
"<div id=\"tooltip\" class=\"tt\" />\n"+
"</div>\n"+
"<script type=\"text/javascript\">\n"+
" loaded();\n"+
"</script>\n"+
"</body>\n"+
"</html>\n"+
"<?php\n"+
" exit (0);\n"+
"} elseif (($serverID = $_SERVER['HTTP_XPDB_DEBUGGER']) != \"0\") {\n"+
"/* * The back end part, invoked from JavaScript using a json call. $serverID is a uniq ID generated from JavaScript * */\n"+
" header(\"Expires: Sat, 1 Jan 2005 00:00:00 GMT\");\n"+
" header(\"Last-Modified: \".gmdate( \"D, d M Y H:i:s\").\" GMT\");\n"+
" header(\"Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0\");\n"+
" header(\"Pragma: no-cache\");\n"+
" header(\"Content-Encoding: identity\");\n"+
" $pdb_dbg = new pdb_JSDebugger((int)$serverID);\n"+
" $pdb_dbg->handleRequests();\n"+
" $pdb_dbg->shutdown();\n"+
" if (PDB_DEBUG) pdb_Logger::debug(\"SERVER TERMINATED!\");\n"+
" exit(0);\n"+
"} else {\n"+
"/* * The front end part, invoked from JavaScript using json calls * */\n"+
" header(\"Expires: Sat, 1 Jan 2005 00:00:00 GMT\");\n"+
" header(\"Last-Modified: \".gmdate( \"D, d M Y H:i:s\").\" GMT\");\n"+
" header(\"Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0\");\n"+
" header(\"Pragma: no-cache\");\n"+
" header(\"Content-Encoding: identity\");\n"+
" pdb_JSDebuggerClient::handleRequest ($_POST);\n"+
" exit(0);\n"+
"}\n"+
"?>\n"+
"";
    public static final byte[] bytes = data.getBytes(); 
}
