
     # makefile_for_spws_programs in ~spws/making

     # $Id: makefile_for_spws_programs,v 1.2 2004/03/04 14:07:43 wjdone Exp $

     # This makefile can be used by any ~spws directory
     # which makes ~spws programs.  Any such ~spws directory
     # need only have a link to this makefile.  For example,
     # you can type the following in the source code directory
     # (under cbyt or va for example) for any program:
     #   ln -s  $SPWSMAKEDIR/makefile_for_spws_programs  makefile

     # This makefile will make an executable with the same name as the
     # parent directory (e.g. cbyt) and any executable with a name
     # beginning with test_.  In either case, the source code
     # containing the main() program must be in a file with the
     # same name as the executable plus the extension .cc.

     # Any special requirements for any individual program can be
     # put into a makefile called makefile.special.  This special
     # makefile does not have to exist.


LINKMODE        = spws
######MATURITY        = production
MATURITY        = beta
PREFIX          = test_
TARGET_PROGRAMS = $(PARENT)

#MYLIBS = /home/spws/lib/linuxab80_debug/libgraphics.a
#MYLIBS = /home/spws/lib/sol62_debug/libgraphics.a

-include ../makefile.special

include $(SPWSMAKEDIR)/makefile.spws


